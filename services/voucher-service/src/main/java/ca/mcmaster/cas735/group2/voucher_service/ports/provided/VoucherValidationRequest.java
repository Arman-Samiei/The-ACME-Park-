package ca.mcmaster.cas735.group2.voucher_service.ports.provided;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationRequestData;

public interface VoucherValidationRequest {
    void validate(VoucherValidationRequestData voucherValidationRequestData);
}
