package ca.mcmaster.cas735.group2.voucher_service.ports.required;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationResponseData;

public interface VoucherValidationResponse {
    void sendValidationResult(VoucherValidationResponseData voucherValidationResponseData);
}
