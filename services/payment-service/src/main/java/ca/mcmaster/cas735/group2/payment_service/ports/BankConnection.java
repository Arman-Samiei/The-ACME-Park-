package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingBankPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;

public interface BankConnection {

    PaymentResponseDTO processBankPayment(OutgoingBankPaymentRequestDTO outgoingBankPaymentRequestDTO);
}
