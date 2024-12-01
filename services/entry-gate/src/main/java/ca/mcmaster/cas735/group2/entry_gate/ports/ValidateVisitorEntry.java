package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateRequestForLotDTO;

public interface ValidateVisitorEntry {

    void sendVisitorEntryValidationRequest(VisitorGateRequestForLotDTO visitorGateRequestForLotDTO);
}
