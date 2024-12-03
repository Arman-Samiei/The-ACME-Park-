package ca.mcmaster.cas735.group2.fines.ports.provided;

import ca.mcmaster.cas735.group2.fines.dto.FinesDeletePaidRecordsData;

public interface FinesDeletePaidRecords {
    void deleteRecords(FinesDeletePaidRecordsData finesDeletePaidRecordsData);
}
