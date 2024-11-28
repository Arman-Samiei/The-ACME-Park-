package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.PermitOrderDTO;

public interface PermitPurchase {

    void processPermitPurchase(PermitOrderDTO permitOrderDTO);
}
