package ca.mcmaster.cas735.group2.exit_gate.dto;

public record VisitorGateActionDTO(
        String plateNumber,
        String lotID,
        String spotID,
        int hoursOccupied,
        String ccNumber,
        String ccExpiry,
        String ccCVC,
        String paymentType
) {}
