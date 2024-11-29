package ca.mcmaster.cas735.group2.lot.ports.provided;

import ca.mcmaster.cas735.group2.lot.dto.LotOccupationStatusUpdateData;

public interface LotOccupationStatusUpdate {
    void updateSpotOccupationStatus(LotOccupationStatusUpdateData lotOccupationStatusUpdateData);
}
