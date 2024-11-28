package ca.mcmaster.cas735.group2.permit.ports.required;

import ca.mcmaster.cas735.group2.permit.dto.PermitLotRequestData;

public interface LotRequest {
    void requestSpot(PermitLotRequestData permitLotRequestData);
}
