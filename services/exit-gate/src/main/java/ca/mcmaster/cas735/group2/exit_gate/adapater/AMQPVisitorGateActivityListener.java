package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.business.ParkingService;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.VisitorGateActivity;
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
public class AMQPVisitorGateActivityListener implements VisitorGateActivity {

    private final ParkingService parkingService;

    @Autowired
    public AMQPVisitorGateActivityListener(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @Override
    public void receiveVisitorGateActivity(VisitorGateActionDTO visitorGateActionDTO) {
        parkingService.validateAndProcessGateAction(visitorGateActionDTO);
    }

    // TODO: Check if private works with @RabbitListener annotation
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.exit.activity.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.qr.request"))
    private void receive(String data, Channel channel, long tag) {
        VisitorGateActionDTO visitorGateActionDTO = convertToDTO(data);

        log.info("Received visitor gate activity: {} - with tag: {} - channel: {}", visitorGateActionDTO, tag, channel);

        receiveVisitorGateActivity(visitorGateActionDTO);
    }

    private VisitorGateActionDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, VisitorGateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
