package ca.mcmaster.cas735.group2.permit.business;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.dto.*;
import ca.mcmaster.cas735.group2.permit.ports.provided.LotResponse;
import ca.mcmaster.cas735.group2.permit.ports.provided.PaymentResponse;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitIssuanceRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.LotRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.PaymentRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitIssuanceResponse;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitRepository;
import ca.mcmaster.cas735.group2.permit.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermitIssuerTest {

    @Mock
    private PermitRepository mockDatabase;

    @Mock
    private LotRequest mockLotRequest;

    @Mock
    private PaymentRequest mockPaymentRequest;

    @Mock
    private PermitIssuanceResponse mockPermitIssuanceResponse;

    @InjectMocks
    private PermitIssuer permitIssuer;

    private PermitIssuanceRequestData testRequestData;
    private PermitData testPermitData;

    @BeforeEach
    void setUp() {
        testRequestData = new PermitIssuanceRequestData();
        testRequestData.setTransponderID("s12345");
        testRequestData.setPlateNumber("PLATE123");
        testRequestData.setFirstName("John");
        testRequestData.setLastName("Doe");
        testRequestData.setEmployeeID("EMP001");
        testRequestData.setLotID("LOT42");
        testRequestData.setMemberPaymentType("credit");
        testRequestData.setCcNumber("1234567812345678");
        testRequestData.setCcExpiry("12/34");
        testRequestData.setCcCVC("123");
        testRequestData.setMonthsPurchased(6);

        testPermitData = testRequestData.asPermitData();
    }

    @Test
    void issue_shouldSavePermitAndRequestSpot() {
        when(mockDatabase.findById("s12345")).thenReturn(Optional.empty());

        permitIssuer.issue(testRequestData);

        verify(mockDatabase).saveAndFlush(any(PermitData.class));
        verify(mockLotRequest).requestSpot(any(PermitLotRequestData.class));
        verifyNoInteractions(mockPermitIssuanceResponse);
    }

    @Test
    void issue_shouldNotSaveDuplicatePermit() {
        when(mockDatabase.findById("s12345")).thenReturn(Optional.of(testPermitData));

        permitIssuer.issue(testRequestData);

        verify(mockDatabase, never()).saveAndFlush(any());
        verify(mockPermitIssuanceResponse).sendPermitIssuanceResponse(
                eq("Permit for the transponderID s12345 has already been issued")
        );
    }

    @Test
    void reserveSpot_shouldHandleNoAvailableSpot() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(testPermitData);

        PermitLotResponseData responseData = new PermitLotResponseData();
        responseData.setPlateNumber("PLATE123");
        responseData.setLotID("LOT42");
        responseData.setSpotID("");

        permitIssuer.reserveSpot(responseData);

        verify(mockPermitIssuanceResponse).sendPermitIssuanceResponse(
                eq("There is no space left for PLATE123 at lot LOT42.")
        );
        verify(mockDatabase).delete(testPermitData);
    }

    @Test
    void reserveSpot_shouldFinalizeSpotReservation() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(testPermitData);

        PermitLotResponseData responseData = new PermitLotResponseData();
        responseData.setPlateNumber("PLATE123");
        responseData.setLotID("LOT42");
        responseData.setSpotID("SPOT1");

        permitIssuer.reserveSpot(responseData);

        ArgumentCaptor<PermitData> captor = ArgumentCaptor.forClass(PermitData.class);
        verify(mockDatabase).saveAndFlush(captor.capture());
        PermitData savedPermit = captor.getValue();

        assertEquals("SPOT1", savedPermit.getSpotID());
        assertEquals("LOT42", savedPermit.getLotID());
    }

    @Test
    void receivePaymentResponse_shouldHandleFailedPayment() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(testPermitData);

        PaymentResponseData responseData = new PaymentResponseData();
        responseData.setPlateNumber("PLATE123");
        responseData.setSuccess(false);

        permitIssuer.receivePaymentResponse(responseData);

        verify(mockPermitIssuanceResponse).sendPermitIssuanceResponse(
                eq("payment unsuccessful for s12345")
        );
        verify(mockLotRequest).requestSpot(any(PermitLotRequestData.class));
        verify(mockDatabase).delete(testPermitData);
    }

    @Test
    void receivePaymentResponse_shouldHandleSuccessfulPayment() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(testPermitData);

        PaymentResponseData responseData = new PaymentResponseData();
        responseData.setPlateNumber("PLATE123");
        responseData.setSuccess(true);

        permitIssuer.receivePaymentResponse(responseData);

        verify(mockPermitIssuanceResponse).sendPermitIssuanceResponse(
                eq("permit successfully issued for s12345")
        );
        verify(mockLotRequest).requestSpot(any(PermitLotRequestData.class));
        verify(mockDatabase).saveAndFlush(any(PermitData.class));
    }
}
