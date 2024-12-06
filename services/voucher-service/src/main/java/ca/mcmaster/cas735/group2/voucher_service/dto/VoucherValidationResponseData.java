package ca.mcmaster.cas735.group2.voucher_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class VoucherValidationResponseData {
    private Boolean shouldOpen;
    private String lotID;
    private String spotID;

    public VoucherValidationResponseData(boolean isValid, String lotID, String spotID) {
        this.shouldOpen = isValid;
        this.lotID = lotID;
        this.spotID = spotID;
    }
}
