package ca.mcmaster.cas735.group2.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PermitIssuanceRequestDTO {

    private String transponderID;
    private String plateNumber;
    private String firstName;
    private String lastName;
    private String employeeID;
    private String lotID;
    private String memberPaymentType;
    private String ccNumber;
    private String ccExpiry;
    private String ccCVC;
    private Integer monthsPurchased;
}
