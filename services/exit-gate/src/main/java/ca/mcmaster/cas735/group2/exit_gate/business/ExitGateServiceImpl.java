package ca.mcmaster.cas735.group2.exit_gate.business;

import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderVoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParkingServiceImpl implements ParkingService {

//    @Autowired TODO
    public ParkingServiceImpl() {}

    @Override
    public void validateAndProcessGateAction(TransponderVoucherGateActionDTO transponderVoucherGateActionDTO) {
        // TODO
    }

    @Override
    public void validateAndProcessGateAction(VisitorGateActionDTO visitorGateActionDTO) {
        // TODO
    }
}
