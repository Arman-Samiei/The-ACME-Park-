package ca.mcmaster.cas735.group2.fines.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FinesData {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long finesID;
    String plateNumber;
    Double amount;

}
