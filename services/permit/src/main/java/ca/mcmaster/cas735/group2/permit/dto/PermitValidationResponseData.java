package ca.mcmaster.cas735.group2.permit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PermitValidationResponseData {
    private Boolean shouldOpen;
    private String lotID;
    private String spotID;


    public PermitValidationResponseData(boolean shouldOpen, String lotID, String spotID) {
        this.shouldOpen = shouldOpen;
        this.lotID = lotID;
        this.spotID = spotID;
    }
}
