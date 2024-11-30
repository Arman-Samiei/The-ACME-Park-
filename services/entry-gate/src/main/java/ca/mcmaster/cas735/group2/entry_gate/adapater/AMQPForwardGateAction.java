package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ForwardGateAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPForwardGateAction implements ForwardGateAction {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPForwardGateAction(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendGateAction(GateActionDTO gateActionDTO) {
        log.info("Sending gate action {} to commit action", gateActionDTO);
        rabbitTemplate.convertAndSend(exchange, gateActionDTO.gateId() + ".action", gateActionDTO);
    }
}
