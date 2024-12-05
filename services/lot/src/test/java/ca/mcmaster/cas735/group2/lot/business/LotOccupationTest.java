package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.dto.LotOccupationStatusUpdateData;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import ca.mcmaster.cas735.group2.lot.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LotOccupationTest {

    @Mock
    private LotRepository mockDatabase;

    private LotOccupation lotOccupation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lotOccupation = new LotOccupation(mockDatabase);
    }

    @Test
    void updateSpotOccupationStatus_shouldUpdateOccupationStatus() {
        LotData lotData = new LotData();
        lotData.setSpotID("SPOT1");
        lotData.setIsSpotOccupied(false);
        lotData.setCustomerType(Constants.VISITOR_CUSTOMER_TYPE);
        lotData.setHasVoucher(false);
        lotData.setSpotReservationStatus(Constants.SPOT_RESERVATION_STATUS_RESERVED);

        when(mockDatabase.findBySpotID("SPOT1")).thenReturn(lotData);

        LotOccupationStatusUpdateData updateData = new LotOccupationStatusUpdateData();
        updateData.setSpotID("SPOT1");
        updateData.setIsSpotOccupied(true);

        lotOccupation.updateSpotOccupationStatus(updateData);

        ArgumentCaptor<LotData> captor = ArgumentCaptor.forClass(LotData.class);
        verify(mockDatabase).saveAndFlush(captor.capture());
        LotData updatedLot = captor.getValue();

        assertEquals(true, updatedLot.getIsSpotOccupied());
        assertEquals(Constants.SPOT_RESERVATION_STATUS_RESERVED, updatedLot.getSpotReservationStatus());
    }

    @Test
    void updateSpotOccupationStatus_shouldResetReservationForVisitorWithoutVoucher() {
        LotData lotData = new LotData();
        lotData.setSpotID("SPOT2");
        lotData.setIsSpotOccupied(true);
        lotData.setCustomerType(Constants.VISITOR_CUSTOMER_TYPE);
        lotData.setHasVoucher(false);
        lotData.setSpotReservationStatus(Constants.SPOT_RESERVATION_STATUS_RESERVED);

        when(mockDatabase.findBySpotID("SPOT2")).thenReturn(lotData);

        LotOccupationStatusUpdateData updateData = new LotOccupationStatusUpdateData();
        updateData.setSpotID("SPOT2");
        updateData.setIsSpotOccupied(false);

        lotOccupation.updateSpotOccupationStatus(updateData);

        ArgumentCaptor<LotData> captor = ArgumentCaptor.forClass(LotData.class);
        verify(mockDatabase).saveAndFlush(captor.capture());
        LotData updatedLot = captor.getValue();

        assertEquals(false, updatedLot.getIsSpotOccupied());
        assertEquals(Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED, updatedLot.getSpotReservationStatus());
    }

    @Test
    void updateSpotOccupationStatus_shouldNotResetReservationForVisitorWithVoucher() {
        LotData lotData = new LotData();
        lotData.setSpotID("SPOT3");
        lotData.setIsSpotOccupied(true);
        lotData.setCustomerType(Constants.VISITOR_CUSTOMER_TYPE);
        lotData.setHasVoucher(true);
        lotData.setSpotReservationStatus(Constants.SPOT_RESERVATION_STATUS_RESERVED);

        when(mockDatabase.findBySpotID("SPOT3")).thenReturn(lotData);

        LotOccupationStatusUpdateData updateData = new LotOccupationStatusUpdateData();
        updateData.setSpotID("SPOT3");
        updateData.setIsSpotOccupied(false);

        lotOccupation.updateSpotOccupationStatus(updateData);

        ArgumentCaptor<LotData> captor = ArgumentCaptor.forClass(LotData.class);
        verify(mockDatabase).saveAndFlush(captor.capture());
        LotData updatedLot = captor.getValue();

        assertEquals(false, updatedLot.getIsSpotOccupied());
        assertEquals(Constants.SPOT_RESERVATION_STATUS_RESERVED, updatedLot.getSpotReservationStatus());
    }
}
