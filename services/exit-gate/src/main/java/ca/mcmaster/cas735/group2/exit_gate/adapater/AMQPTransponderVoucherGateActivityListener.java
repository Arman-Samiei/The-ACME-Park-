package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.business.ParkingService;
import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderVoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.TransponderVoucherGateActivity;
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
public class AMQPTransponderVoucherGateActivityListener implements TransponderVoucherGateActivity {

    private final ParkingService parkingService;

    @Autowired
    public AMQPTransponderVoucherGateActivityListener(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @Override
    public void receiveTransponderVoucherGateActivity(TransponderVoucherGateActionDTO transponderVoucherGateActionDTO) {
        parkingService.validateAndProcessGateAction(transponderVoucherGateActionDTO);
    }

    // TODO: Check if private works with @RabbitListener annotation
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.exit.activity.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.transponder-voucher.request"))
    private void receive(String data, Channel channel, long tag) {
        TransponderVoucherGateActionDTO transponderVoucherGateActionDTO = convertToDTO(data);

        log.info("Received transponder gate activity: {} - with tag: {} - channel: {}", transponderVoucherGateActionDTO, tag, channel);

        receiveTransponderVoucherGateActivity(transponderVoucherGateActionDTO);
    }

    private TransponderVoucherGateActionDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, TransponderVoucherGateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
