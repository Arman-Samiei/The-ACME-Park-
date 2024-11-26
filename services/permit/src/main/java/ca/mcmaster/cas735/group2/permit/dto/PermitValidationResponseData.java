package ca.mcmaster.cas735.group2.permit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class PermitValidationResponseData {
    private Boolean isValid;
    private String message;

    public PermitValidationResponseData(boolean b, String the_permit_is_valid) {
        isValid = b;
        message = the_permit_is_valid;
    }
}
