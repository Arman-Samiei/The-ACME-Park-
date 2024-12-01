package ca.mcmaster.cas735.group2.fines.ports.provided;

import ca.mcmaster.cas735.group2.fines.dto.FinesIssuanceData;

public interface FinesIssuance {
    void issueFine(FinesIssuanceData finesIssuanceData);
}
