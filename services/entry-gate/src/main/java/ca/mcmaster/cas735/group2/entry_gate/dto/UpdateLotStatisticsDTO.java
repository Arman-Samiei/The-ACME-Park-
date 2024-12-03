package ca.mcmaster.cas735.group2.entry_gate.dto;

public record UpdateLotStatisticsDTO(
        String spotID,
        boolean isSpotOccupied
) {}
