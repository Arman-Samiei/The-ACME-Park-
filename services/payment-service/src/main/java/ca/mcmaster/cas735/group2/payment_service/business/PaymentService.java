package ca.mcmaster.cas735.group2.payment_service.business;

import ca.mcmaster.cas735.group2.payment_service.dto.ExistingFinesDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;

public interface PaymentService {

    void createOrderAndDetermineFines(PaymentRequestDTO paymentRequestDTO);
    void commitOrderAndRoute(ExistingFinesDTO existingFinesDTO);
}
