package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.business.ParkingService;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.VoucherGateActivity;
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
public class AMQPVoucherGateActivityListener implements VoucherGateActivity {

    private final ParkingService parkingService;

    @Autowired
    public AMQPVoucherGateActivityListener(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @Override
    public void receiveVoucherGateActivity(VoucherGateActionDTO voucherGateActionDTO) {
        parkingService.validateAndProcessGateAction(voucherGateActionDTO);
    }

    // TODO: Check if private works with @RabbitListener annotation
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.entry.activity.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.voucher.request"))
    private void receive(String data, Channel channel, long tag) {
        VoucherGateActionDTO voucherGateActionDTO = convertToDTO(data);

        log.info("Received gate activity: {} - with tag: {} - channel: {}", voucherGateActionDTO, tag, channel);

        receiveVoucherGateActivity(voucherGateActionDTO);
    }

    private VoucherGateActionDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, VoucherGateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
