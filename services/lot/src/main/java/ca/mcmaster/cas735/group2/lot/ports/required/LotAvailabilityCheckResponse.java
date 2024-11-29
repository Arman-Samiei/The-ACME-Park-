package ca.mcmaster.cas735.group2.lot.ports.required;

import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityResponseData;

public interface LotAvailabilityCheckResponse {
    void sendAvailableSpot(LotAvailabilityResponseData lotAvailabilityResponseData, String responseReceiver);
}
