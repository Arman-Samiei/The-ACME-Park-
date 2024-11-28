package ca.mcmaster.cas735.group2.permit.ports.provided;

import ca.mcmaster.cas735.group2.permit.dto.PaymentResponseData;

public interface PaymentResponse {
    void receivePaymentResponse(PaymentResponseData paymentResponseData);
}
