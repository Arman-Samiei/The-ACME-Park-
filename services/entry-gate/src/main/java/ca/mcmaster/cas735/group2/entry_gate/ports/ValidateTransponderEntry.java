package ca.mcmaster.cas735.group2.entry_gate.ports;

public interface ValidateTransponderEntry {

    void sendTransponderEntryValidationRequest(String transponderId);
}
