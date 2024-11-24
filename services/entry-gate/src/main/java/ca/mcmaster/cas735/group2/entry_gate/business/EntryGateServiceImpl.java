package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateTransponderEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVoucherEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EntryGateServiceImpl implements EntryGateService {

    private final ValidateTransponderEntry validateTransponderEntry;
    private final ValidateVoucherEntry validateVoucherEntry;

    @Autowired
    public EntryGateServiceImpl(ValidateTransponderEntry validateTransponderEntry,
                                ValidateVoucherEntry validateVoucherEntry) {
        this.validateTransponderEntry = validateTransponderEntry;
        this.validateVoucherEntry = validateVoucherEntry;
    }

    @Override
    public void validateAndProcessGateAction(TransponderGateActionDTO transponderGateActionDTO) {
        validateTransponderEntry.sendTransponderEntryValidationRequest(transponderGateActionDTO.transponderId());
    }

    @Override
    public void validateAndProcessGateAction(VoucherGateActionDTO voucherGateActionDTO) {
        validateVoucherEntry.sendVoucherEntryValidationRequest(voucherGateActionDTO.voucherId());
    }

    @Override
    public String validateAndProcessGateActionForVisitor() {
        String qrId = UUID.randomUUID().toString();
        // QR Code is generated and sent to the visitor, they can enter the gate
        processGateAction(true, qrId);
        return qrId;
    }

    private void processGateAction(boolean shouldOpen, String id) {
        if (shouldOpen) {
            log.info("Gate opened for {}", id);
        } else {
            log.info("Gate remained closed for {}", id);
        }
    }
}
