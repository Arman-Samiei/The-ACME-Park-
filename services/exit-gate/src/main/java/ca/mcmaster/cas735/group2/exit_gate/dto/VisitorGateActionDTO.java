package ca.mcmaster.cas735.group2.exit_gate.dto;

public record VisitorGateActionDTO(
        String qrId,
        String ccNumber,
        String ccExpiry,
        String ccCVC,
        double paymentAmount,
        String paymentType
) {}
