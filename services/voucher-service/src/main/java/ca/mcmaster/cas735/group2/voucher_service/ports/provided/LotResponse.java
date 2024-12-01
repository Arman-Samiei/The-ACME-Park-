package ca.mcmaster.cas735.group2.voucher_service.ports.provided;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotResponseData;

public interface LotResponse {
    void reserveSpot(VoucherLotResponseData voucherLotResponseData);
}