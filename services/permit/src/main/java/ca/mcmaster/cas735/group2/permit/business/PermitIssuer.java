package ca.mcmaster.cas735.group2.permit.business;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.dto.*;
import ca.mcmaster.cas735.group2.permit.ports.provided.LotResponse;
import ca.mcmaster.cas735.group2.permit.ports.provided.PaymentResponse;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitIssuanceRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.LotRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.PaymentRequest;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitIssuanceResponse;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class PermitIssuer implements PermitIssuanceRequest, LotResponse, PaymentResponse {

    private final PermitRepository database;
    private final LotRequest lotRequest;

    private final PaymentRequest paymentRequest;
    private final PermitIssuanceResponse permitIssuanceResponse;

    @Autowired
    public PermitIssuer(PermitRepository database, LotRequest lotRequest, PaymentRequest paymentRequest, PermitIssuanceResponse permitIssuanceResponse) {
        this.database = database;
        this.lotRequest = lotRequest;
        this.paymentRequest = paymentRequest;
        this.permitIssuanceResponse = permitIssuanceResponse;

    }

    @Override
    public void issue(PermitIssuanceRequestData permitIssuanceRequestData) {
        String paymentType = permitIssuanceRequestData.getPaymentType();
        PermitData permitData = permitIssuanceRequestData.asPermitData();
        String customerType = permitData.getCustomerType();
        String transponderID = permitData.getTransponderID();
        if (Objects.equals(customerType, "student") && Objects.equals(paymentType, "Uni")) {
            String response = String.format("%s is for a student and does not have a payslip.", transponderID);
            permitIssuanceResponse.sendPermitIssuanceResponse(response);
            log.debug(response);
            return;
        }
        database.saveAndFlush(permitData);
        lotRequest.requestSpot(new PermitLotRequestData(permitData, "pending"));
    }

    @Override
    public void reserveSpot(PermitLotResponseData permitLotResponseData) {
        String plateNumber = permitLotResponseData.getPlateNumber();
        String spotID = permitLotResponseData.getSpotID();
        String lotID = permitLotResponseData.getLotID();
        PermitData permitData = database.findByPlateNumber(plateNumber);
        if (spotID.isEmpty()) {
            String response = String.format("There is no space left for %s at lot %s.", plateNumber, lotID);
            permitIssuanceResponse.sendPermitIssuanceResponse(response);
            database.delete(permitData);
            return;
        }
        permitData.setSpotID(spotID);
        database.saveAndFlush(permitData);
        paymentRequest.requestPayment(new PaymentRequestData(permitData.getPlateNumber()));
    }

    @Override
    public void receivePaymentResponse(PaymentResponseData paymentResponseData) {
        Boolean wasSuccessful = paymentResponseData.getWasSuccessful();
        PermitData permitData = database.findByPlateNumber(paymentResponseData.getPlateNumber());
        if (!wasSuccessful) {
            String response = String.format("payment unsuccessful for %s", permitData.getTransponderID());
            permitIssuanceResponse.sendPermitIssuanceResponse(response);
            lotRequest.requestSpot(new PermitLotRequestData(permitData, "notConfirmed"));
            database.delete(permitData);
            return;
        }
        permitData.setStatus("issued");
        database.saveAndFlush(permitData);
        String response = String.format("permit successfully issued for %s", permitData.getTransponderID());
        lotRequest.requestSpot(new PermitLotRequestData(permitData, "confirmed"));
        permitIssuanceResponse.sendPermitIssuanceResponse(response);

    }

}