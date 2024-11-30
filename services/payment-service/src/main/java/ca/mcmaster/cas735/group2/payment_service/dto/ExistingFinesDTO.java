package ca.mcmaster.cas735.group2.payment_service.dto;

public record ExistingFinesDTO(
        String id,
        String licensePlate,
        double fineAmount
) {}
