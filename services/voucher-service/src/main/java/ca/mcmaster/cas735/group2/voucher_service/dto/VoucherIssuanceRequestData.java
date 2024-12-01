package ca.mcmaster.cas735.group2.voucher_service.dto;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VoucherIssuanceRequestData {

    private String plateNumber;
    private String lotID;

    public VoucherData toVoucherData() {
        VoucherData result = new VoucherData();
        result.setPlateNumber(this.plateNumber);
        result.setLotID(this.lotID);
        result.setStatus("pending");
        result.setSpotID(null);
        result.setExpirationTime(LocalDateTime.now().plusDays(3));
        return result;
    }

}
