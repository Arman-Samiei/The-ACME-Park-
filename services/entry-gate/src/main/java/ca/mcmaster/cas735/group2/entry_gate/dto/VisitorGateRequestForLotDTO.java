package ca.mcmaster.cas735.group2.entry_gate.dto;

public record VisitorGateRequestForLotDTO(
        String lotID,
        String requestSender,
        String plateNumber
) {}
