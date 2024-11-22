package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParkingServiceImpl implements ParkingService {

//    @Autowired TODO
    public ParkingServiceImpl() {}

    @Override
    public void validateAndProcessGateAction(TransponderGateActionDTO transponderGateActionDTO) {
        // TODO
    }

    @Override
    public void validateAndProcessGateAction(VisitorGateActionDTO visitorGateActionDTO) {
        // TODO
    }

    @Override
    public void validateAndProcessGateAction(VoucherGateActionDTO voucherGateActionDTO) {
        // TODO
    }
}
