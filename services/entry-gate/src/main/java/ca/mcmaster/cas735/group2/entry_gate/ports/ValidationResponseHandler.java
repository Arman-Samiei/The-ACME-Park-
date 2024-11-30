package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.GateActionDTO;

public interface ValidationResponseHandler {

    void forwardValidationToGate(GateActionDTO gateActionDTO);
}
