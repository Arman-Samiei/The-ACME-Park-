package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;

public interface VisitorGateActivity {

    void receiveVisitorGateActivity(VisitorGateActionDTO visitorGateActionDTO);
}
