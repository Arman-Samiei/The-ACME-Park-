package ca.mcmaster.cas735.group2.voucher_service.business;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherIssuanceRequestData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotRequestData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotResponseData;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.LotRequest;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherIssuanceResponse;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoucherIssuerTest {

    @Mock
    private VoucherRepository mockDatabase;

    @Mock
    private LotRequest mockLotRequest;

    @Mock
    private VoucherIssuanceResponse mockVoucherIssuanceResponse;

    @InjectMocks
    private VoucherIssuer voucherIssuer;

    private VoucherIssuanceRequestData requestData;
    private VoucherData voucherData;

    @BeforeEach
    void setUp() {
        requestData = new VoucherIssuanceRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");
        requestData.setDays(3);

        voucherData = requestData.toVoucherData();
    }

    @Test
    void issue_shouldSendRequestForNewVoucher() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(null);

        voucherIssuer.issue(requestData);

        ArgumentCaptor<VoucherLotRequestData> captor = ArgumentCaptor.forClass(VoucherLotRequestData.class);
        verify(mockLotRequest).requestSpot(captor.capture());

        VoucherLotRequestData lotRequestData = captor.getValue();
        assertEquals("LOT42", lotRequestData.getLotID());
        assertEquals("PLATE123", lotRequestData.getPlateNumber());
        verify(mockDatabase).saveAndFlush(any(VoucherData.class));
    }

    @Test
    void issue_shouldRespondIfVoucherAlreadyIssued() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(voucherData);

        voucherIssuer.issue(requestData);

        verify(mockVoucherIssuanceResponse).sendVoucherIssuanceResponse("Voucher for the plate number PLATE123 has already been issued");
        verifyNoInteractions(mockLotRequest);
        verify(mockDatabase, never()).saveAndFlush(any());
    }

    @Test
    void reserveSpot_shouldFinalizeVoucherWhenSpotAvailable() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(voucherData);

        VoucherLotResponseData responseData = new VoucherLotResponseData();
        responseData.setPlateNumber("PLATE123");
        responseData.setLotID("LOT42");
        responseData.setSpotID("SPOT1");

        voucherIssuer.reserveSpot(responseData);

        ArgumentCaptor<VoucherData> captor = ArgumentCaptor.forClass(VoucherData.class);
        verify(mockDatabase).saveAndFlush(captor.capture());

        VoucherData updatedVoucher = captor.getValue();
        assertEquals("SPOT1", updatedVoucher.getSpotID());
        assertEquals("issued", updatedVoucher.getStatus());
        verify(mockVoucherIssuanceResponse).sendVoucherIssuanceResponse("Voucher Issued for PLATE123 plate number at LOT42 lotID. SpotID: SPOT1");
    }

    @Test
    void reserveSpot_shouldHandleNoAvailableSpot() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(voucherData);

        VoucherLotResponseData responseData = new VoucherLotResponseData();
        responseData.setPlateNumber("PLATE123");
        responseData.setLotID("LOT42");
        responseData.setSpotID("");

        voucherIssuer.reserveSpot(responseData);

        verify(mockVoucherIssuanceResponse).sendVoucherIssuanceResponse("There is no space left for PLATE123 at lot LOT42.");
        verify(mockDatabase).delete(voucherData);
    }
}
