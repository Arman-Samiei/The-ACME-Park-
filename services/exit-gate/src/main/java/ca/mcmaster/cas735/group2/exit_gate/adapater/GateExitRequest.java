package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.business.ExitGateService;
import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.VoucherGateActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/exit/request")
public class GateExitRequest implements TransponderGateActivity, VisitorGateActivity, VoucherGateActivity {

    private final ExitGateService exitGateService;

    @Autowired
    public GateExitRequest(ExitGateService exitGateService) {
        this.exitGateService = exitGateService;
    }

    @PostMapping(value = "/transponder")
    @Override
    @ResponseStatus(HttpStatus.OK)
    public void receiveTransponderGateActivity(@RequestBody TransponderGateActionDTO transponderGateActionDTO) {
        exitGateService.processTransponderGateAction(transponderGateActionDTO);
    }

    @PostMapping(value = "/visitor")
    @Override
    @ResponseStatus(HttpStatus.OK)
    public void receiveVisitorGateActivity(@RequestBody VisitorGateActionDTO visitorGateActionDTO) {
        exitGateService.validateVisitorGateAction(visitorGateActionDTO);
    }

    @PostMapping(value = "/voucher")
    @Override
    @ResponseStatus(HttpStatus.OK)
    public void receiveVoucherGateActivity(@RequestBody VoucherGateActionDTO voucherGateActionDTO) {
        exitGateService.processVoucherGateAction(voucherGateActionDTO);
    }
}
