package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;

public interface ParkingService {

    void validateAndProcessGateAction(TransponderGateActionDTO transponderGateActionDTO);
    void validateAndProcessGateAction(VisitorGateActionDTO visitorGateActionDTO);
    void validateAndProcessGateAction(VoucherGateActionDTO voucherGateActionDTO);
}
