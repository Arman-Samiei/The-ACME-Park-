package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;

public interface EntryGateService {

    void validateAndProcessGateAction(TransponderGateActionDTO transponderGateActionDTO);
    void validateAndProcessGateAction(VoucherGateActionDTO voucherGateActionDTO);
    String validateAndProcessGateActionForVisitor(VisitorGateActionDTO visitorGateActionDTO);
    void forwardGateResponse(GateActionDTO gateActionDTO);
}
