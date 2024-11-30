package ca.mcmaster.cas735.group2.payment_service.dto;

public record OutgoingBankPaymentRequestDTO(
        String ccNumber,
        String ccExpiry,
        String ccCVC,
        double paymentAmount
) {}
