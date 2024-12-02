package ca.mcmaster.cas735.group2.permit.business;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.dto.PermitValidationRequestData;
import ca.mcmaster.cas735.group2.permit.dto.PermitValidationResponseData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitValidationRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitRepository;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class PermitValidator implements PermitValidationRequest {
    private final PermitRepository database;
    private final PermitValidationResponse permitValidationResponse;

    @Autowired
    public PermitValidator(PermitRepository database, PermitValidationResponse permitValidationResponse) {
        this.database = database;
        this.permitValidationResponse = permitValidationResponse;
    }

    @Override
    public void validate(PermitValidationRequestData permitValidationRequestData) {
        String licencePlate = permitValidationRequestData.getLicencePlate();
        String lotID = permitValidationRequestData.getLotID();
        PermitData permitData = database.findByPlateNumber(licencePlate);
        PermitValidationResponseData permitValidationResponseData;
        if (permitData != null && Objects.equals(permitData.getLotID(), lotID) && Objects.equals(permitData.getStatus(), "issued"))
            permitValidationResponseData = new PermitValidationResponseData(true, String.format("The permit associated with the plate number %s is valid", licencePlate));
        else
            permitValidationResponseData = new PermitValidationResponseData(false, String.format("The permit associated with the plate number %s is not valid. FUCK OFF!", licencePlate));
        permitValidationResponse.sendValidationResult(permitValidationResponseData);
    }

}
