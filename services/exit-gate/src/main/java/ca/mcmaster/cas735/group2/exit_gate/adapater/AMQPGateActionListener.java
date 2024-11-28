package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.business.ExitGateService;
import ca.mcmaster.cas735.group2.exit_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.GateAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPGateActionListener implements GateAction {

    private final ExitGateService exitGateService;

    @Autowired
    public AMQPGateActionListener(ExitGateService exitGateService) {
        this.exitGateService = exitGateService;
    }

    @Override
    public void handleGateAction(GateActionDTO gateActionDTO) {
        exitGateService.processGateAction(gateActionDTO.shouldOpen(), gateActionDTO.lot());
    }

    // TODO: Check if private works with @RabbitListener annotation
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.exit.action", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.exit"))
    private void receive(String data, Channel channel, long tag) {
        GateActionDTO gateActionDTO = convertToDTO(data);
        log.info("Received gate action: {} - with tag: {} - channel: {}", gateActionDTO, tag, channel);
        handleGateAction(gateActionDTO);
    }

    private GateActionDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, GateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
