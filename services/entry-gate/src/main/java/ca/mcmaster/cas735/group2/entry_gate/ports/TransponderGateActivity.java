package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;

public interface TransponderGateActivity {

    void receiveTransponderGateActivity(TransponderGateActionDTO transponderGateActionDTO);
}
