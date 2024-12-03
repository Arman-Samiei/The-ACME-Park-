package ca.mcmaster.cas735.group2.permit.dto;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitLotRequestData {
    String lotID;
    String customerType;
    String plateNumber;
    String spotReservationStatus;

    String requestSender = "permit";

    public PermitLotRequestData(PermitData permitData, String spotReservationStatus) {
        this.lotID = permitData.getLotID();
        this.customerType = permitData.getMemberRole();
        this.plateNumber = permitData.getPlateNumber();
        this.spotReservationStatus = spotReservationStatus;
    }
}