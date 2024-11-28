package ca.mcmaster.cas735.group2.payment_service.dto;

public record PaymentRequestDTO(
    String id,
    String ccNumber,
    String ccExpiry,
    String ccCVC,
    double paymentAmount,
    String paymentType
) {}
