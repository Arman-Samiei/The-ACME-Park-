package ca.mcmaster.cas735.group2.permit.ports.required;

import ca.mcmaster.cas735.group2.permit.dto.PermitValidationResponseData;

public interface PermitValidationResponse {
    void sendValidationResult(PermitValidationResponseData permitValidationResponseData);
}
