package ca.mcmaster.cas735.group2.fines.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinesDeletePaidRecordsData {
    String plateNumber;
    Boolean isPaid;
}
