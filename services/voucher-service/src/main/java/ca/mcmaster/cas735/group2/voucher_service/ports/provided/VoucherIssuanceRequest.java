package ca.mcmaster.cas735.group2.voucher_service.ports.provided;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherIssuanceRequestData;

public interface VoucherIssuanceRequest {

    void issue(VoucherIssuanceRequestData voucherIssuanceRequestData);
}
