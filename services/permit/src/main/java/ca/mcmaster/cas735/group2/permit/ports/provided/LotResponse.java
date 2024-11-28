package ca.mcmaster.cas735.group2.permit.ports.provided;

import ca.mcmaster.cas735.group2.permit.dto.PermitLotResponseData;

public interface LotResponse {
    void reserveSpot(PermitLotResponseData permitLotResponseData);
}