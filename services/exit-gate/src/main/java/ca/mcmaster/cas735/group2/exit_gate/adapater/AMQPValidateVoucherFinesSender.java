package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
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
    public void sendVoucherValidationForFines(VisitorGateActionDTO voucherAsVisitorDTO) {
        log.info("Checking fines for voucher {}", voucherAsVisitorDTO);
        rabbitTemplate.convertAndSend(exchange, "payment.activity.request", translate(voucherAsVisitorDTO));
    }

    private String translate(VisitorGateActionDTO voucherAsVisitorDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(voucherAsVisitorDTO);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
