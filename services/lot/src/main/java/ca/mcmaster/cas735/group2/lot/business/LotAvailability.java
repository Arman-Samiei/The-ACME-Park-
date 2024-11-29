package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityRequestData;
import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityResponseData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotAvailabilityCheckRequest;
import ca.mcmaster.cas735.group2.lot.ports.required.LotAvailabilityCheckResponse;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class LotAvailability implements LotAvailabilityCheckRequest {

    private final LotRepository database;
    private final LotAvailabilityCheckResponse responseSender;

    @Autowired
    public LotAvailability(LotRepository database, LotAvailabilityCheckResponse responseSender) {
        this.database = database;
        this.responseSender = responseSender;
    }

    @Override
    public void checkLotAvailability(LotAvailabilityRequestData requestData) {
        String requestSender = requestData.getRequestSender();
        String requestDataSpotReservationStatus = requestData.getSpotReservationStatus();

        if (isPermitRequest(requestSender))
            handlePermitRequest(requestData, requestDataSpotReservationStatus);
        else
            handleQrRequest(requestData);

    }

    private boolean isPermitRequest(String requestSender) {
        return "permit".equals(requestSender);
    }

    private void handlePermitRequest(LotAvailabilityRequestData requestData, String requestDataSpotReservationStatus) {
        if ("pending".equals(requestDataSpotReservationStatus))
            handlePendingRequest(requestData);
        else if ("confirmed".equals(requestDataSpotReservationStatus))
            handleConfirmedRequest(requestData);
        else if ("notConfirmed".equals(requestDataSpotReservationStatus))
            handleNotConfirmedRequest(requestData);

    }

    private void handlePendingRequest(LotAvailabilityRequestData requestData) {
        LotData availableSpot = findAvailableSpot(requestData);

        if (availableSpot == null) {
            sendEmptyResponse("permit");
            return;
        }

        updateSpot(availableSpot, "pending", requestData.getPlateNumber());
        sendSpotResponse(availableSpot, "permit");
    }

    private void handleConfirmedRequest(LotAvailabilityRequestData requestData) {
        String plateNumber = requestData.getPlateNumber();
        LotData spot = database.findByPlateNumber(plateNumber);
        updateSpot(spot, "confirmed", plateNumber);
    }

    private void handleNotConfirmedRequest(LotAvailabilityRequestData requestData) {
        String plateNumber = requestData.getPlateNumber();
        LotData spot = database.findByPlateNumber(plateNumber);
        updateSpot(spot, "notReserved", plateNumber);
    }

    private void handleQrRequest(LotAvailabilityRequestData requestData) {
        LotData availableSpot = findAvailableSpot(requestData);

        if (availableSpot == null) {
            sendEmptyResponse("qr");
            return;
        }

        updateSpot(availableSpot, "confirmed", requestData.getPlateNumber());
        sendSpotResponse(availableSpot, "qr");
    }

    private LotData findAvailableSpot(LotAvailabilityRequestData requestData) {
        return database.findFirstByCustomerTypeAndSpotReservationStatusAndLotID(
                requestData.getCustomerType(),
                "notReserved",
                requestData.getLotID()
        );
    }

    private void updateSpot(LotData spot, String status, String plateNumber) {
        spot.setSpotReservationStatus(status);
        spot.setPlateNumber(plateNumber);
        database.saveAndFlush(spot);
    }

    private void sendSpotResponse(LotData spot, String recipient) {
        LotAvailabilityResponseData responseData = new LotAvailabilityResponseData(spot);
        responseSender.sendAvailableSpot(responseData, recipient);
    }

    private void sendEmptyResponse(String recipient) {
        responseSender.sendAvailableSpot(LotAvailabilityResponseData.emptyResponse(), recipient);
    }
}
