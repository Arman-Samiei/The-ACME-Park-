package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.UpdateLotStatisticsDTO;

public interface LotStatistics {

    void updateEntryLotStatistics(UpdateLotStatisticsDTO updateLotStatisticsDTO);
}
