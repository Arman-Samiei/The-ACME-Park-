package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import ca.mcmaster.cas735.group2.lot.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotReleaseTest {

    private SpotRelease spotRelease;

    @Mock
    private LotRepository database;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spotRelease = new SpotRelease(database);
    }

    @Test
    void releaseSpot_shouldUpdateSpotDetails() {
        String plateNumber = "PLATE123";

        LotData mockLotData = new LotData();
        mockLotData.setPlateNumber(plateNumber);
        mockLotData.setSpotReservationStatus(Constants.SPOT_RESERVATION_STATUS_RESERVED);

        when(database.findByPlateNumber(plateNumber)).thenReturn(mockLotData);

        spotRelease.releaseSpot(plateNumber);

        assertNull(mockLotData.getPlateNumber());
        assertEquals(Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED, mockLotData.getSpotReservationStatus());

        verify(database).findByPlateNumber(plateNumber);
        verify(database).saveAndFlush(mockLotData);
    }
}
