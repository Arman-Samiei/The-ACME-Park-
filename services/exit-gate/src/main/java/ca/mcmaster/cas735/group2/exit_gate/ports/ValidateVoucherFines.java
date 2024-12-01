package ca.mcmaster.cas735.group2.exit_gate.ports;

import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;

public interface ValidateVoucherFines {

    void sendVoucherValidationForFines(VoucherGateActionDTO voucherGateActionDTO);
}
