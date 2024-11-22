package ca.mcmaster.cas735.group2.exit_gate.ports;

import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderVoucherGateActionDTO;

public interface TransponderVoucherGateActivity {

    void receiveTransponderVoucherGateActivity(TransponderVoucherGateActionDTO transponderVoucherGateActionDTO);
}
