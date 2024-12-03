package ca.mcmaster.cas735.group2.permit.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

@Entity @Data public class PermitData {
    @Id
    private String transponderID;

    private String plateNumber;

    private String firstName;

    private String lastName;

    private String employeeID;

    private String lotID;

    private String spotID;

    private String status;

    private String memberPaymentType;

    private String memberRole;

    private String ccNumber;

    private String ccExpiry;

    private String ccCVC;

    private Integer monthsPurchased;

    private LocalDateTime expirationTime;
}


