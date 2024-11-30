package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.business.EntryGateService;
import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.VoucherGateActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/entry/request")
public class GateEntryRequest implements TransponderGateActivity, VisitorGateActivity, VoucherGateActivity {

    private final EntryGateService entryGateService;

    @Autowired
    public GateEntryRequest(EntryGateService entryGateService) {
        this.entryGateService = entryGateService;
    }

    @PostMapping(value = "/transponder")
    @Override
    @ResponseStatus(HttpStatus.OK)
    public void receiveTransponderGateActivity(@RequestBody TransponderGateActionDTO transponderGateActionDTO) {
        entryGateService.validateAndProcessGateAction(transponderGateActionDTO);
    }

    @PostMapping(value = "/visitor")
    @Override
    @ResponseStatus(HttpStatus.OK)
    public String receiveVisitorGateActivity(@RequestBody VisitorGateActionDTO visitorGateActionDTO) {
        return entryGateService.validateAndProcessGateActionForVisitor(visitorGateActionDTO);
    }

    @PostMapping(value = "/voucher")
    @Override
    @ResponseStatus(HttpStatus.OK)
    public void receiveVoucherGateActivity(@RequestBody VoucherGateActionDTO voucherGateActionDTO) {
        entryGateService.validateAndProcessGateAction(voucherGateActionDTO);
    }
}
