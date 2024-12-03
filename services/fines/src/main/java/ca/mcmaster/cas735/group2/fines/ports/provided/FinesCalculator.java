package ca.mcmaster.cas735.group2.fines.ports.provided;

import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationRequestData;

public interface FinesCalculator {
    void calculateTotalFine(FinesCalculationRequestData finesCalculationRequestData);
}
