package ca.mcmaster.cas735.group2.voucher_service.ports.required;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotRequestData;

public interface LotRequest {
    void requestSpot(VoucherLotRequestData voucherLotRequestData);
}
