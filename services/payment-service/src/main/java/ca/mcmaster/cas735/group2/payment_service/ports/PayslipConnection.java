package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;

public interface PayslipConnection {

    void processPayment(PaymentRequestDTO paymentRequestDTO);
}
