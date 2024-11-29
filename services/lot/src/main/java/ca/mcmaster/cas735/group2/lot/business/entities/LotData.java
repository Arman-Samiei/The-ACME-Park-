package ca.mcmaster.cas735.group2.lot.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class LotData {
    @Id
    String spotID;
    String lotID;
    String customerType;
    String plateNumber;
    String spotOccupationStatus;
    String spotReservationStatus;
}


