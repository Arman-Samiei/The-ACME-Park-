package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;

import java.net.URISyntaxException;

public interface PaymentActivity {

    void receivePaymentActivity(PaymentRequestDTO paymentRequestDTO) throws URISyntaxException;
}
