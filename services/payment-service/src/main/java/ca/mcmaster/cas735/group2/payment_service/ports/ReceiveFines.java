package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.ExistingFinesDTO;

public interface ReceiveFines {

    void commitOrderAndRoute(ExistingFinesDTO existingFinesDTO);
}
