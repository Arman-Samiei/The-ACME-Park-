package ca.mcmaster.cas735.group2.voucher_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class VoucherValidationResponseData {
    private Boolean shouldOpen;

    private String lotID;

    public VoucherValidationResponseData(boolean isValid, String lotID) {
        this.shouldOpen = isValid;
        this.lotID = lotID;
    }
}
