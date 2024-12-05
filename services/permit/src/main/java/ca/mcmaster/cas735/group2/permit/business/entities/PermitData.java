package ca.mcmaster.cas735.group2.permit.business.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "permit")
public class PermitData {
    @Id
    @Column(name = "TRANSPONDER_ID")
    private String transponderID;

    private String plateNumber;

    private String firstName;

    private String lastName;

    @Column(name = "EMPLOYEE_ID")
    private String employeeID;
    @Column(name = "LOT_ID")
    private String lotID;
    @Column(name = "SPOT_ID")
    private String spotID;

    private String status;

    private String memberPaymentType;

    private String memberRole;

    private String ccNumber;

    private String ccExpiry;

    @Column(name = "CC_CVC")
    private String ccCVC;

    private Integer monthsPurchased;

    private LocalDateTime expirationTime;
}


