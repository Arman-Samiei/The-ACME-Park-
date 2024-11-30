package ca.mcmaster.cas735.group2.payment_service.dto;

public record PaymentRequestDTO(
    String id,
    String gateId,
    String staffId,
    String licensePlate,
    String ccNumber,
    String ccExpiry,
    String ccCVC,
    int hoursOccupied,
    int monthsPurchased,
    String paymentType
) {}
