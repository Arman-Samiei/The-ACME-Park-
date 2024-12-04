package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateLotResponseDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.VisitorLotResponse;
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
public class AMQPVisitorLotResponseListener {

    private final VisitorLotResponse visitorLotResponse;

    @Autowired
    public AMQPVisitorLotResponseListener(VisitorLotResponse visitorLotResponse) {
        this.visitorLotResponse = visitorLotResponse;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "visitor.lot.response.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "visitor.lot.response"))
    public void receive(String data, Channel channel) {
        VisitorGateLotResponseDTO visitorGateRequestForLotDTO = convertToDTO(data);
        log.info("Received lot response for visitor: {} - channel: {}", visitorGateRequestForLotDTO, channel);
        visitorLotResponse.sendVisitorLotResponse(visitorGateRequestForLotDTO);
    }

    private VisitorGateLotResponseDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, VisitorGateLotResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
