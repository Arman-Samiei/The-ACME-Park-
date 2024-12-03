package ca.mcmaster.cas735.group2.permit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PermitValidationResponseData {
    private Boolean shouldOpen;
    private String lotID;


    public PermitValidationResponseData(boolean shouldOpen, String lotID) {
        this.shouldOpen = shouldOpen;
        this.lotID = lotID;
    }
}
