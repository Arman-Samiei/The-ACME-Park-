package ca.mcmaster.cas735.group2.payment_service.business;

import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;

public interface PaymentService {

    void createOrderAndProcessPayment(PaymentRequestDTO paymentRequestDTO);
    void comfirmOrderAndRoute(PaymentResponseDTO paymentResponseDTO);
}
