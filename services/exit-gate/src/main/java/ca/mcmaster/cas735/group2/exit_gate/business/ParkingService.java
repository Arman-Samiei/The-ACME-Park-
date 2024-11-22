package ca.mcmaster.cas735.group2.exit_gate.business;

import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderVoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;

public interface ParkingService {

    void validateAndProcessGateAction(TransponderVoucherGateActionDTO transponderVoucherGateActionDTO);
    void validateAndProcessGateAction(VisitorGateActionDTO visitorGateActionDTO);
}
