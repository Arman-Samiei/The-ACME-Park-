package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingBankPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingPayslipPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.BankConnection;
import ca.mcmaster.cas735.group2.payment_service.ports.PayslipConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class PaymentRequest implements BankConnection, PayslipConnection {

    @Value("${app.bank-route}")
    private String bankRoute;

    @Value("${app.payslip-route}")
    private String payslipRoute;

    @Override
    public PaymentResponseDTO processBankPayment(OutgoingBankPaymentRequestDTO outgoingBankPaymentRequestDTO) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            URI uri = new URI(bankRoute);
            return restTemplate.postForObject(uri, outgoingBankPaymentRequestDTO, PaymentResponseDTO.class);
        } catch (URISyntaxException e) {
            log.error("Error processing bank payment: {}", e.getMessage());
            return new PaymentResponseDTO(false);
        }
    }

    @Override
    public PaymentResponseDTO processPayslipPayment(OutgoingPayslipPaymentRequestDTO outgoingPayslipPaymentRequestDTO) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            URI uri = new URI(payslipRoute);
            return restTemplate.postForObject(uri, outgoingPayslipPaymentRequestDTO, PaymentResponseDTO.class);
        } catch (URISyntaxException e) {
            log.error("Error processing payslip payment: {}", e.getMessage());
            return new PaymentResponseDTO(false);
        }
    }
}
