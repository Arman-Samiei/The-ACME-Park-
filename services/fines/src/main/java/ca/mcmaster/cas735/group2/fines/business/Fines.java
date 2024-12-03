package ca.mcmaster.cas735.group2.fines.business;

import ca.mcmaster.cas735.group2.fines.business.entities.FinesData;
import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationRequestData;
import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationResponseData;
import ca.mcmaster.cas735.group2.fines.dto.FinesDeletePaidRecordsData;
import ca.mcmaster.cas735.group2.fines.dto.FinesIssuanceData;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesCalculator;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesDeletePaidRecords;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesIssuance;
import ca.mcmaster.cas735.group2.fines.ports.required.FinesCalculationResponse;
import ca.mcmaster.cas735.group2.fines.ports.required.FinesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Fines implements FinesCalculator, FinesIssuance, FinesDeletePaidRecords {
    private final FinesCalculationResponse finesCalculationResponse;
    private final FinesRepository database;

    @Autowired
    public Fines(FinesCalculationResponse finesCalculationResponse, FinesRepository database) {
        this.finesCalculationResponse = finesCalculationResponse;
        this.database = database;
    }

    @Override
    public void calculateTotalFine(FinesCalculationRequestData finesCalculationRequestData) {
        String plateNumber = finesCalculationRequestData.getPlateNumber();
        String id = finesCalculationRequestData.getId();
        Double totalAmount = database.findTotalAmountByPlateNumber(plateNumber);
        FinesCalculationResponseData finesCalculationResponseData = new FinesCalculationResponseData(id, plateNumber, totalAmount);
        finesCalculationResponse.sendResponse(finesCalculationResponseData);
    }

    @Override
    public void issueFine(FinesIssuanceData finesIssuanceData) {
        FinesData finesData = finesIssuanceData.toFinesData();
        database.saveAndFlush(finesData);
    }

    @Override
    public void deleteRecords(FinesDeletePaidRecordsData finesDeletePaidRecordsData) {
        String plateNumber = finesDeletePaidRecordsData.getPlateNumber();
        Boolean isPaid = finesDeletePaidRecordsData.getIsPaid();
        if (isPaid)
            database.deleteByPlateNumber(plateNumber);
    }


}
