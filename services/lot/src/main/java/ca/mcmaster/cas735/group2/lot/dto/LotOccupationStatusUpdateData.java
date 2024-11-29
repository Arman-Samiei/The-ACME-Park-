package ca.mcmaster.cas735.group2.lot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class LotOccupationStatusUpdateData {
    String spotID;
    String occupationStatus;
}
