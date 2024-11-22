package ca.mcmaster.cas735.group2.entry_gate.ports;

import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;

public interface VoucherGateActivity {

    void receiveVoucherGateActivity(VoucherGateActionDTO voucherGateActionDTO);
}
