package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.FinesRequestDTO;

public interface DetermineFines {

    void requestFineAmount(FinesRequestDTO finesRequestDTO);
}
