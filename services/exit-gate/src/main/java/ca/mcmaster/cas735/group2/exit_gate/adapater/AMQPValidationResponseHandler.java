package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidationResponseHandler;
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
public class AMQPValidationResponseHandler {

    private final ValidationResponseHandler validationResponseHandler;

    @Autowired
    public AMQPValidationResponseHandler(ValidationResponseHandler validationResponseHandler) {
        this.validationResponseHandler = validationResponseHandler;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.exit.action.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.exit.action"))
    public void receive(String data, Channel channel) {
        GateActionDTO gateActionDTO = convertToDTO(data);
        log.info("Received gate action to forward: {} - channel: {}", gateActionDTO, channel);
        validationResponseHandler.forwardValidationToGate(gateActionDTO);
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
