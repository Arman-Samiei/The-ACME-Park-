package ca.mcmaster.cas735.group2.exit_gate.ports;

import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;

public interface VoucherGateActivity {

    void receiveVoucherGateActivity(VoucherGateActionDTO voucherGateActionDTO);
}
