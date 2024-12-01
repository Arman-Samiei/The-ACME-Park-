package ca.mcmaster.cas735.group2.voucher_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class VoucherValidationResponseData {
    private Boolean isValid;
    private String message;

    public VoucherValidationResponseData(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }
}
