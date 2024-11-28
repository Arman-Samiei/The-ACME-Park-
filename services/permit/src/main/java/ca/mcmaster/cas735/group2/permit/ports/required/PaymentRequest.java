package ca.mcmaster.cas735.group2.permit.ports.required;

import ca.mcmaster.cas735.group2.permit.dto.PaymentRequestData;

public interface PaymentRequest {
    void requestPayment(PaymentRequestData paymentRequestData);
}
