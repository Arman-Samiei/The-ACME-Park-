package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.LotStatistics;
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
    private final LotStatistics lotStatistics;

    @Autowired
    public EntryGateServiceImpl(ValidateTransponderEntry validateTransponderEntry,
                                ValidateVoucherEntry validateVoucherEntry,
                                LotStatistics lotStatistics) {
        this.validateTransponderEntry = validateTransponderEntry;
        this.validateVoucherEntry = validateVoucherEntry;
        this.lotStatistics = lotStatistics;
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
        processGateAction(true, "A");
        return qrId;
    }

    @Override
    public void processGateAction(boolean shouldOpen, String lot) {
        if (shouldOpen) {
            log.info("Entry Gate opened");
            lotStatistics.updateEntryLotStatistics("entry");
        } else {
            log.info("Entry Gate remained closed");
        }
    }
}
