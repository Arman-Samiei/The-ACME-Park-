package ca.mcmaster.cas735.group2.exit_gate.business;

import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;

public interface ExitGateService {

    void validateAndProcessGateAction(TransponderGateActionDTO transponderGateActionDTO);
    void validateAndProcessGateAction(VoucherGateActionDTO voucherGateActionDTO);
    void validateAndProcessGateAction(VisitorGateActionDTO visitorGateActionDTO);
    void processGateAction(boolean shouldOpen, String lot);
}
