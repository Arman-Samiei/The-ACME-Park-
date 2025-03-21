package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVoucherEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPVoucherValidationSender implements ValidateVoucherEntry {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPVoucherValidationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendVoucherEntryValidationRequest(VoucherGateActionDTO voucherGateActionDTO) {
        log.info("Asking validation for voucher {}", voucherGateActionDTO);
        rabbitTemplate.convertAndSend(exchange, "voucher.entry.validation", translate(voucherGateActionDTO));
    }

    private String translate(VoucherGateActionDTO voucherGateActionDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(voucherGateActionDTO);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

//    @Bean
//    public TopicExchange outbound() {
//        // this will create the outbound exchange if it does not exist
//        return new TopicExchange(exchange);
//    }
}
