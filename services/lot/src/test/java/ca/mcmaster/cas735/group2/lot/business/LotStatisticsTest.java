package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LotStatisticsTest {

    private LotStatistics lotStatistics;

    @Mock
    private LotRepository database;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lotStatistics = new LotStatistics(database);
    }

    @Test
    void getAllSpots_shouldReturnAllSpots() {
        LotData spot1 = new LotData();
        LotData spot2 = new LotData();
        List<LotData> spots = Arrays.asList(spot1, spot2);

        when(database.findAll()).thenReturn(spots);

        List<LotData> result = lotStatistics.getAllSpots();

        assertEquals(2, result.size());
        assertEquals(spots, result);
        verify(database).findAll();
    }

    @Test
    void getOccupiedSpots_shouldReturnOnlyOccupiedSpots() {
        LotData occupiedSpot = new LotData();
        occupiedSpot.setIsSpotOccupied(true);

        LotData freeSpot = new LotData();
        freeSpot.setIsSpotOccupied(false);

        List<LotData> spots = Arrays.asList(occupiedSpot, freeSpot);

        when(database.findAll()).thenReturn(spots);

        List<LotData> result = lotStatistics.getOccupiedSpots();

        assertEquals(1, result.size());
        assertEquals(occupiedSpot, result.get(0));
        verify(database).findAll();
    }

    @Test
    void getFreeSpots_shouldReturnOnlyFreeSpots() {
        LotData occupiedSpot = new LotData();
        occupiedSpot.setIsSpotOccupied(true);

        LotData freeSpot = new LotData();
        freeSpot.setIsSpotOccupied(false);

        List<LotData> spots = Arrays.asList(occupiedSpot, freeSpot);

        when(database.findAll()).thenReturn(spots);

        List<LotData> result = lotStatistics.getFreeSpots();

        assertEquals(1, result.size());
        assertEquals(freeSpot, result.get(0));
        verify(database).findAll();
    }
}
