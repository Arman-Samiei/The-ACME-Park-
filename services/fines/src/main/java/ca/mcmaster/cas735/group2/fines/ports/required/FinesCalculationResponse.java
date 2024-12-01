package ca.mcmaster.cas735.group2.fines.ports.required;

import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationResponseData;

public interface FinesCalculationResponse {
    void sendResponse(FinesCalculationResponseData finesCalculationResponseData);
}
