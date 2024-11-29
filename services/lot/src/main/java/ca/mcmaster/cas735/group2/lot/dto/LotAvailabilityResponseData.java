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

    public static LotAvailabilityResponseData emptyResponse() {
        LotData emptyLotData = new LotData();
        emptyLotData.setLotID("");
        emptyLotData.setSpotID("");
        emptyLotData.setPlateNumber("");
        return new LotAvailabilityResponseData(emptyLotData);
    }
}
