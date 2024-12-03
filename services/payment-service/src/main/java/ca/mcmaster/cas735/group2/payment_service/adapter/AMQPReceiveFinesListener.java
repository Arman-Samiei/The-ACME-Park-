package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.ExistingFinesDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.ReceiveFines;
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
public class AMQPReceiveFinesListener {

    private final ReceiveFines receiveFines;

    @Autowired
    public AMQPReceiveFinesListener(ReceiveFines receiveFines) {
        this.receiveFines = receiveFines;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "fines.payment.response.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "fines.payment.response"))
    private void receive(String data, Channel channel, long tag) {
        ExistingFinesDTO existingFinesDTO = convertToDTO(data);

        log.info("Received fines: {} - with tag: {} - channel: {}", existingFinesDTO, tag, channel);

        receiveFines.commitOrderAndRoute(existingFinesDTO);
    }

    private ExistingFinesDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, ExistingFinesDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
