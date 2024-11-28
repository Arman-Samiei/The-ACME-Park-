package ca.mcmaster.cas735.group2.permit.ports.provided;

import ca.mcmaster.cas735.group2.permit.dto.PermitIssuanceRequestData;

public interface PermitIssuanceRequest {

    void issue(PermitIssuanceRequestData permitIssuanceRequestData);
}
