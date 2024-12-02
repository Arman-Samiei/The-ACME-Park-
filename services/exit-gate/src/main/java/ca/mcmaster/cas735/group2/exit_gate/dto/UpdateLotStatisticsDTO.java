package ca.mcmaster.cas735.group2.exit_gate.dto;

public record UpdateLotStatisticsDTO(
        String spotId,
        boolean isSpotOccupied
) {}
