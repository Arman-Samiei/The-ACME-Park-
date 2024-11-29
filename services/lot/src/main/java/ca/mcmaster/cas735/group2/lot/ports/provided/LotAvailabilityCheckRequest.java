package ca.mcmaster.cas735.group2.lot.ports.provided;

import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityRequestData;

public interface LotAvailabilityCheckRequest {
    void checkLotAvailability(LotAvailabilityRequestData lotAvailabilityRequestData);
}
