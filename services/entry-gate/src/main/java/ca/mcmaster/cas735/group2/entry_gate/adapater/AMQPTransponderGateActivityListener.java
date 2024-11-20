package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.business.ParkingService;
import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.TransponderGateActivity;
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
public class AMQPTransponderGateActivityListener implements TransponderGateActivity {

    private final ParkingService parkingService;

    @Autowired
    public AMQPTransponderGateActivityListener(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @Override
    public void receiveTransponderGateActivity(TransponderGateActionDTO transponderGateActionDTO) {
        parkingService.validateAndProcessGateAction(transponderGateActionDTO);
    }

    // TODO: Check if private works with @RabbitListener annotation
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.activity.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.transponder.request"))
    private void receive(String data, Channel channel, long tag) {
        TransponderGateActionDTO transponderGateActionDTO = convertToDTO(data);

        log.info("Received transponder gate activity: {} - with tag: {} - channel: {}", transponderGateActionDTO, tag, channel);

        receiveTransponderGateActivity(transponderGateActionDTO);
    }

    private TransponderGateActionDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, TransponderGateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
