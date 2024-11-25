package ca.mcmaster.cas735.group2.exit_gate.ports;

import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;

public interface TransponderGateActivity {

    void receiveTransponderGateActivity(TransponderGateActionDTO transponderGateActionDTO);
}
