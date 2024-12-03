package ca.mcmaster.cas735.group2.exit_gate.dto;

public record VoucherGateActionDTO(
        String plateNumber,
        String spotID,
        String lotID,
        String ccNumber,
        String ccExpiry,
        String ccCVC
) {}
