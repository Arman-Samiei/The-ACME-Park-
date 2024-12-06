package ca.mcmaster.cas735.group2.voucher_service.business;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationRequestData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationResponseData;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherRepository;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class VoucherValidatorTest {

    @Mock
    private VoucherRepository mockDatabase;

    @Mock
    private VoucherValidationResponse mockValidationResponse;

    private VoucherValidator voucherValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        voucherValidator = new VoucherValidator(mockDatabase, mockValidationResponse);
    }

    @Test
    void validate_shouldSendValidationResultTrueWhenVoucherIsValid() {
        VoucherValidationRequestData requestData = new VoucherValidationRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");

        VoucherData voucherData = new VoucherData();
        voucherData.setPlateNumber("PLATE123");
        voucherData.setLotID("LOT42");
        voucherData.setSpotID("SPOT1");

        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(voucherData);

        voucherValidator.validate(requestData);

        ArgumentCaptor<VoucherValidationResponseData> responseCaptor = ArgumentCaptor.forClass(VoucherValidationResponseData.class);
        verify(mockValidationResponse).sendValidationResult(responseCaptor.capture());

        VoucherValidationResponseData capturedResponse = responseCaptor.getValue();
        assertEquals(true, capturedResponse.getShouldOpen());
        assertEquals("LOT42", capturedResponse.getLotID());
        assertEquals("SPOT1", capturedResponse.getSpotID());
    }

    @Test
    void validate_shouldSendValidationResultFalseWhenVoucherIsInvalid() {
        VoucherValidationRequestData requestData = new VoucherValidationRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");

        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(null);

        voucherValidator.validate(requestData);

        ArgumentCaptor<VoucherValidationResponseData> responseCaptor = ArgumentCaptor.forClass(VoucherValidationResponseData.class);
        verify(mockValidationResponse).sendValidationResult(responseCaptor.capture());

        VoucherValidationResponseData capturedResponse = responseCaptor.getValue();
        assertEquals(false, capturedResponse.getShouldOpen());
        assertEquals("LOT42", capturedResponse.getLotID());
        assertNull(capturedResponse.getSpotID());
    }

    @Test
    void validate_shouldSendValidationResultFalseWhenLotIDDoesNotMatch() {
        VoucherValidationRequestData requestData = new VoucherValidationRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");

        VoucherData voucherData = new VoucherData();
        voucherData.setPlateNumber("PLATE123");
        voucherData.setLotID("LOT99");

        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(voucherData);

        voucherValidator.validate(requestData);

        ArgumentCaptor<VoucherValidationResponseData> responseCaptor = ArgumentCaptor.forClass(VoucherValidationResponseData.class);
        verify(mockValidationResponse).sendValidationResult(responseCaptor.capture());

        VoucherValidationResponseData capturedResponse = responseCaptor.getValue();
        assertEquals(false, capturedResponse.getShouldOpen());
        assertEquals("LOT42", capturedResponse.getLotID());
        assertNull(capturedResponse.getSpotID());
    }
}
