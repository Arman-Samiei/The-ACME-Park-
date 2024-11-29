package ca.mcmaster.cas735.group2.lot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class LotAvailabilityRequestData {
    private String lotID;
    private String customerType;
    private String plateNumber;
    private String spotReservationStatus;
    private String requestSender;
}
