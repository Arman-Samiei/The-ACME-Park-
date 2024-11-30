package ca.mcmaster.cas735.group2.exit_gate.business;

import ca.mcmaster.cas735.group2.exit_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;

public interface ExitGateService {

    void processTransponderGateAction(TransponderGateActionDTO transponderGateActionDTO);
    void processVoucherGateAction(VoucherGateActionDTO voucherGateActionDTO);
    void validateVisitorGateAction(VisitorGateActionDTO visitorGateActionDTO);
    void processGateAction(GateActionDTO gateActionDTO);
}
