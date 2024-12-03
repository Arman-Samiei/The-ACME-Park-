package ca.mcmaster.cas735.group2.entry_gate.dto;

public record GateActionDTO(
        boolean shouldOpen,
        String lotID,
        String spotID,
        boolean isVisitor
) {}
