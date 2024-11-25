package ca.mcmaster.cas735.group2.exit_gate.ports;

import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;

public interface ValidateVisitorExit {

    void sendVisitorExitValidationRequest(VisitorGateActionDTO visitorGateActionDTO);
}
