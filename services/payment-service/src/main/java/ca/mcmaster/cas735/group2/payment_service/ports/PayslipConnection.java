package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingPayslipPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;

public interface PayslipConnection {

    PaymentResponseDTO processPayslipPayment(OutgoingPayslipPaymentRequestDTO outgoingPayslipPaymentRequestDTO);
}
