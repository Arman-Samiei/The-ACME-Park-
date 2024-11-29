package ca.mcmaster.cas735.group2.lot.ports.provided;


import ca.mcmaster.cas735.group2.lot.business.entities.LotData;

import java.util.List;

public interface LotStatisticsRequest {
    List<LotData> getAllSpots();
    List<LotData> getOccupiedSpots();
    List<LotData> getFreeSpots();
}
