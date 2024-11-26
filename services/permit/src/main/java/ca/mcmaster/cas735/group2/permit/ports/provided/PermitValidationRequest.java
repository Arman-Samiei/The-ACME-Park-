package ca.mcmaster.cas735.group2.permit.ports.provided;

import ca.mcmaster.cas735.group2.permit.dto.PermitValidationRequestData;

public interface PermitValidationRequest {
    void validate(PermitValidationRequestData permitValidationData);
}
