package ca.mcmaster.cas735.group2.lot.dto;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LotAvailabilityResponseData {
    private String lotID;
    private String spotID;
    private String plateNumber;

    public LotAvailabilityResponseData(LotData lotData) {
        this.lotID = lotData.getLotID();
        this.spotID = lotData.getSpotID();
        this.plateNumber = lotData.getPlateNumber();
    }

    public static LotAvailabilityResponseData emptyResponse(String lotID, String plateNumber) {
        LotData emptyLotData = new LotData();
        emptyLotData.setLotID(lotID);
        emptyLotData.setSpotID("");
        emptyLotData.setPlateNumber(plateNumber);
        return new LotAvailabilityResponseData(emptyLotData);
    }
}
