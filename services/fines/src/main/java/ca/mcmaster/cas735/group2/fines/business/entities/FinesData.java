package ca.mcmaster.cas735.group2.fines.business.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "fines")
public class FinesData {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "FINES_ID")
    Long finesID;
    String plateNumber;
    Double amount;

}
