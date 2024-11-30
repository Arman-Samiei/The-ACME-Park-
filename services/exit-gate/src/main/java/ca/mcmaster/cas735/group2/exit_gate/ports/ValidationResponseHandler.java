package ca.mcmaster.cas735.group2.exit_gate.ports;

import ca.mcmaster.cas735.group2.exit_gate.dto.GateActionDTO;

public interface ValidationResponseHandler {

    void sendGateAction(GateActionDTO gateActionDTO);
}
