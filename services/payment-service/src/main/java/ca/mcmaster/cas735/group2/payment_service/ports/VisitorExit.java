package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.GateActionDTO;

public interface VisitorExit {

    void processVisitorExit(GateActionDTO gateActionDTO);
}
