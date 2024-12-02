package ca.mcmaster.cas735.group2.payment_service.dto;

public record PermitOrderDTO(
        String plateNumber,
        boolean success
) {}
