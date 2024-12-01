package ca.mcmaster.cas735.group2.voucher_service.dto;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherLotRequestData {
    String lotID;

    String customerType = "visitor";
    String plateNumber;

    String accessPassProcessingStatus = "confirmed";

    String requestSender = "voucher";

    public VoucherLotRequestData(VoucherData voucherData) {
        this.lotID = voucherData.getLotID();
        this.plateNumber = voucherData.getPlateNumber();
    }
}