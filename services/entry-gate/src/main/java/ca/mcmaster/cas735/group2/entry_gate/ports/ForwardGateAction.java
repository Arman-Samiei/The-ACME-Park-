package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.GateActionDTO;

public interface ForwardGateAction {

    void sendGateAction(GateActionDTO gateActionDTO);
}
