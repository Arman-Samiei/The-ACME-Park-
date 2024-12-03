package ca.mcmaster.cas735.group2.permit.dto;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import lombok.Data;
import lombok.NoArgsConstructor;

import static ca.mcmaster.cas735.group2.permit.utils.Constants.LOT_SERVICE_REQUEST_SENDER;

@Data
@NoArgsConstructor
public class PermitLotRequestData {
    String lotID;
    String customerType;
    String plateNumber;
    String spotReservationStatus;

    String requestSender = LOT_SERVICE_REQUEST_SENDER;

    public PermitLotRequestData(PermitData permitData, String spotReservationStatus) {
        this.lotID = permitData.getLotID();
        this.customerType = permitData.getMemberRole();
        this.plateNumber = permitData.getPlateNumber();
        this.spotReservationStatus = spotReservationStatus;
    }
}