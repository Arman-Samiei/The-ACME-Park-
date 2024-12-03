package ca.mcmaster.cas735.group2.permit.business;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.dto.PermitValidationRequestData;
import ca.mcmaster.cas735.group2.permit.dto.PermitValidationResponseData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitValidationRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitRepository;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitValidationResponse;
import ca.mcmaster.cas735.group2.permit.utils.Constants;
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
        String plateNumber = permitValidationRequestData.getPlateNumber();
        String lotID = permitValidationRequestData.getLotID();
        PermitData permitData = database.findByPlateNumber(plateNumber);
        PermitValidationResponseData permitValidationResponseData;
        if (permitData != null && Objects.equals(permitData.getLotID(), lotID) && Objects.equals(permitData.getStatus(), Constants.ISSUED_PERMIT_STATUS))
            permitValidationResponseData = new PermitValidationResponseData(true, lotID);
        else
            permitValidationResponseData = new PermitValidationResponseData(false, lotID);
        permitValidationResponse.sendValidationResult(permitValidationResponseData);
    }

}
