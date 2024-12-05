package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityRequestData;
import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityResponseData;
import ca.mcmaster.cas735.group2.lot.ports.required.LotAvailabilityCheckResponse;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import ca.mcmaster.cas735.group2.lot.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LotAvailabilityTest {

    @Mock
    private LotRepository mockDatabase;

    @Mock
    private LotAvailabilityCheckResponse mockResponseSender;

    private LotAvailability lotAvailability;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lotAvailability = new LotAvailability(mockDatabase, mockResponseSender);
    }

    @Test
    void checkLotAvailability_shouldHandlePermitPendingRequest() {
        LotData availableSpot = new LotData();
        availableSpot.setSpotID("SPOT1");
        availableSpot.setLotID("LOT1");
        availableSpot.setSpotReservationStatus(Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED);

        LotAvailabilityRequestData requestData = new LotAvailabilityRequestData();
        requestData.setLotID("LOT1");
        requestData.setCustomerType(Constants.SENDER_RECEIVER_PERMIT);
        requestData.setRequestSender(Constants.SENDER_RECEIVER_PERMIT);
        requestData.setAccessPassProcessingStatus(Constants.ACCESS_PASS_PROCESSING_STATUS_PENDING);
        requestData.setPlateNumber("PLATE123");

        when(mockDatabase.findFirstByCustomerTypeAndSpotReservationStatusAndLotID(
                Constants.SENDER_RECEIVER_PERMIT,
                Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED,
                "LOT1"
        )).thenReturn(availableSpot);

        lotAvailability.checkLotAvailability(requestData);

        ArgumentCaptor<LotData> spotCaptor = ArgumentCaptor.forClass(LotData.class);
        verify(mockDatabase).saveAndFlush(spotCaptor.capture());
        LotData updatedSpot = spotCaptor.getValue();

        assertEquals("SPOT1", updatedSpot.getSpotID());
        assertEquals(Constants.SPOT_RESERVATION_STATUS_PENDING, updatedSpot.getSpotReservationStatus());
        assertEquals("PLATE123", updatedSpot.getPlateNumber());

        ArgumentCaptor<LotAvailabilityResponseData> responseCaptor = ArgumentCaptor.forClass(LotAvailabilityResponseData.class);
        verify(mockResponseSender).sendAvailableSpot(responseCaptor.capture(), eq(Constants.SENDER_RECEIVER_PERMIT));
        LotAvailabilityResponseData response = responseCaptor.getValue();

        assertEquals("SPOT1", response.getSpotID());
    }

    @Test
    void checkLotAvailability_shouldSendEmptyResponseWhenNoSpotFound() {
        LotAvailabilityRequestData requestData = new LotAvailabilityRequestData();
        requestData.setLotID("LOT1");
        requestData.setCustomerType(Constants.SENDER_RECEIVER_VISITOR);
        requestData.setRequestSender(Constants.SENDER_RECEIVER_VISITOR);
        requestData.setPlateNumber("PLATE123");

        when(mockDatabase.findFirstByCustomerTypeAndSpotReservationStatusAndLotID(
                Constants.SENDER_RECEIVER_VISITOR,
                Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED,
                "LOT1"
        )).thenReturn(null);

        lotAvailability.checkLotAvailability(requestData);

        ArgumentCaptor<LotAvailabilityResponseData> responseCaptor = ArgumentCaptor.forClass(LotAvailabilityResponseData.class);
        verify(mockResponseSender).sendAvailableSpot(responseCaptor.capture(), eq(Constants.SENDER_RECEIVER_VISITOR));
        LotAvailabilityResponseData response = responseCaptor.getValue();

        assertEquals("", response.getSpotID());
        assertEquals("LOT1", response.getLotID());
        assertEquals("PLATE123", response.getPlateNumber());
    }

    @Test
    void checkLotAvailability_shouldHandleConfirmedPermitRequest() {
        LotData existingSpot = new LotData();
        existingSpot.setSpotID("SPOT1");
        existingSpot.setLotID("LOT1");
        existingSpot.setPlateNumber("PLATE123");

        LotAvailabilityRequestData requestData = new LotAvailabilityRequestData();
        requestData.setLotID("LOT1");
        requestData.setRequestSender(Constants.SENDER_RECEIVER_PERMIT);
        requestData.setAccessPassProcessingStatus(Constants.ACCESS_PASS_PROCESSING_STATUS_CONFIRMED);
        requestData.setPlateNumber("PLATE123");

        when(mockDatabase.findByPlateNumber("PLATE123")).thenReturn(existingSpot);

        lotAvailability.checkLotAvailability(requestData);

        verify(mockDatabase).saveAndFlush(existingSpot);
        assertEquals(Constants.SPOT_RESERVATION_STATUS_RESERVED, existingSpot.getSpotReservationStatus());
    }
}
