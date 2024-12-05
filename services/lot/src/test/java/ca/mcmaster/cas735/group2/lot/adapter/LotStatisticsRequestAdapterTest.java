package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotStatisticsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LotStatisticsRequestAdapterTest {

    private LotStatisticsRequestAdapter adapter;

    @Mock
    private LotStatisticsRequest lotStatisticsRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new LotStatisticsRequestAdapter(lotStatisticsRequest);
    }

    @Test
    void getOccupiedSpots_shouldReturnOccupiedSpots() {
        List<LotData> occupiedSpots = Arrays.asList(
                createLotData("SPOT1", "LOT1", true, "Reserved"),
                createLotData("SPOT2", "LOT2", true, "Reserved")
        );
        when(lotStatisticsRequest.getOccupiedSpots()).thenReturn(occupiedSpots);

        List<LotData> result = adapter.getOccupiedSpots();

        verify(lotStatisticsRequest).getOccupiedSpots();
        assertEquals(occupiedSpots, result);
    }

    @Test
    void getFreeSpots_shouldReturnFreeSpots() {
        List<LotData> freeSpots = Arrays.asList(
                createLotData("SPOT3", "LOT1", false, "NotReserved"),
                createLotData("SPOT4", "LOT2", false, "NotReserved")
        );
        when(lotStatisticsRequest.getFreeSpots()).thenReturn(freeSpots);

        List<LotData> result = adapter.getFreeSpots();

        verify(lotStatisticsRequest).getFreeSpots();
        assertEquals(freeSpots, result);
    }

    @Test
    void getAllSpots_shouldReturnAllSpots() {
        List<LotData> allSpots = Arrays.asList(
                createLotData("SPOT1", "LOT1", true, "Reserved"),
                createLotData("SPOT3", "LOT1", false, "NotReserved"),
                createLotData("SPOT2", "LOT2", true, "Reserved"),
                createLotData("SPOT4", "LOT2", false, "NotReserved")
        );
        when(lotStatisticsRequest.getAllSpots()).thenReturn(allSpots);

        List<LotData> result = adapter.getAllSpots();

        verify(lotStatisticsRequest).getAllSpots();
        assertEquals(allSpots, result);
    }

    private LotData createLotData(String spotID, String lotID, Boolean isOccupied, String reservationStatus) {
        LotData lotData = new LotData();
        lotData.setSpotID(spotID);
        lotData.setLotID(lotID);
        lotData.setIsSpotOccupied(isOccupied);
        lotData.setSpotReservationStatus(reservationStatus);
        return lotData;
    }
}
