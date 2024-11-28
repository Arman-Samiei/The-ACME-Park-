package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;

public interface PaymentResponse {

    void handlePaymentResponse(PaymentResponseDTO paymentResponseDTO);
}
