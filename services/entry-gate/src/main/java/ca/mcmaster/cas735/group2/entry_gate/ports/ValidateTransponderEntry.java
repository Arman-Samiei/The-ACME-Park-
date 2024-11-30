package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;

public interface ValidateTransponderEntry {

    void sendTransponderEntryValidationRequest(TransponderGateActionDTO transponderGateActionDTO);
}
