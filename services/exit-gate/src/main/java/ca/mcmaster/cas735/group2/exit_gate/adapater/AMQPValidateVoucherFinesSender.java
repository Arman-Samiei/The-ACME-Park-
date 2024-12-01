package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateVoucherFines;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPValidateVoucherFinesSender implements ValidateVoucherFines {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPValidateVoucherFinesSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendVoucherValidationForFines(VoucherGateActionDTO voucherGateActionDTO) {
        log.info("Checking fines for voucher {}", voucherGateActionDTO);
        rabbitTemplate.convertAndSend(exchange, "payment.activity.request", translate(voucherGateActionDTO));
    }

    private String translate(VoucherGateActionDTO voucherGateActionDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(voucherGateActionDTO);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
