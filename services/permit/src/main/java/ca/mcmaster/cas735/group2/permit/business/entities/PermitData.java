package ca.mcmaster.cas735.group2.permit.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity @Data public class PermitData {
    @Id
    private String transponderID;

    private String PlateNumber;

    private String firstName;

    private String lastName;

    private String employeeID;

    private String lotID;
}


