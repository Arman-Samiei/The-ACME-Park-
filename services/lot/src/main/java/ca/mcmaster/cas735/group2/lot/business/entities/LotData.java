package ca.mcmaster.cas735.group2.lot.business.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "spots")
public class LotData {
    @Column(name = "SPOT_ID")
    @Id
    String spotID;
    @Column(name = "LOT_ID")
    String lotID;
    String customerType;
    String plateNumber;
    Boolean isSpotOccupied;
    String spotReservationStatus;
    Boolean hasVoucher;
}