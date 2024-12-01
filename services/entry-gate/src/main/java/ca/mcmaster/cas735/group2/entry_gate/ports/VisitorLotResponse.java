package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateLotResponseDTO;

public interface VisitorLotResponse {

    void sendVisitorLotResponse(VisitorGateLotResponseDTO visitorGateLotResponseDTO);
}
