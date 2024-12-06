package ca.mcmaster.cas735.group2.permit.business;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.dto.PermitValidationRequestData;
import ca.mcmaster.cas735.group2.permit.dto.PermitValidationResponseData;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitRepository;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitValidationResponse;
import ca.mcmaster.cas735.group2.permit.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermitValidatorTest {

    @Mock
    private PermitRepository mockDatabase;

    @Mock
    private PermitValidationResponse mockPermitValidationResponse;

    @InjectMocks
    private PermitValidator permitValidator;

    private PermitValidationRequestData validRequestData;
    private PermitData validPermitData;

    @BeforeEach
    void setUp() {
        validRequestData = new PermitValidationRequestData();
        validRequestData.setPlateNumber("PLATE123");
        validRequestData.setLotID("LOT42");


        validPermitData = new PermitData();
        validPermitData.setPlateNumber("PLATE123");
        validPermitData.setLotID("LOT42");
        validPermitData.setStatus(Constants.ISSUED_PERMIT_STATUS);
        validPermitData.setSpotID("SPOT1");
    }

    @Test
    void validate_shouldSendSuccessResponseForValidPermit() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(validPermitData);

        permitValidator.validate(validRequestData);

        ArgumentCaptor<PermitValidationResponseData> captor = ArgumentCaptor.forClass(PermitValidationResponseData.class);
        verify(mockPermitValidationResponse).sendValidationResult(captor.capture());

        PermitValidationResponseData response = captor.getValue();
        assertEquals(true, response.getShouldOpen());
        assertEquals("LOT42", response.getLotID());
        assertEquals("SPOT1", response.getSpotID());
    }

    @Test
    void validate_shouldSendFailureResponseForNonMatchingLotID() {
        validPermitData.setLotID("LOT99");
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(validPermitData);

        permitValidator.validate(validRequestData);

        ArgumentCaptor<PermitValidationResponseData> captor = ArgumentCaptor.forClass(PermitValidationResponseData.class);
        verify(mockPermitValidationResponse).sendValidationResult(captor.capture());

        PermitValidationResponseData response = captor.getValue();
        assertEquals(false, response.getShouldOpen());
        assertEquals("LOT42", response.getLotID());
        assertNull(response.getSpotID());
    }

    @Test
    void validate_shouldSendFailureResponseForNonIssuedStatus() {
        validPermitData.setStatus(Constants.PENDING_PERMIT_STATUS);
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(validPermitData);

        permitValidator.validate(validRequestData);

        ArgumentCaptor<PermitValidationResponseData> captor = ArgumentCaptor.forClass(PermitValidationResponseData.class);
        verify(mockPermitValidationResponse).sendValidationResult(captor.capture());

        PermitValidationResponseData response = captor.getValue();
        assertEquals(false, response.getShouldOpen());
        assertEquals("LOT42", response.getLotID());
        assertNull(response.getSpotID());
    }

    @Test
    void validate_shouldSendFailureResponseForNonExistingPermit() {
        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(null);

        permitValidator.validate(validRequestData);

        ArgumentCaptor<PermitValidationResponseData> captor = ArgumentCaptor.forClass(PermitValidationResponseData.class);
        verify(mockPermitValidationResponse).sendValidationResult(captor.capture());

        PermitValidationResponseData response = captor.getValue();
        assertEquals(false, response.getShouldOpen());
        assertEquals("LOT42", response.getLotID());
        assertNull(response.getSpotID());
    }
}
