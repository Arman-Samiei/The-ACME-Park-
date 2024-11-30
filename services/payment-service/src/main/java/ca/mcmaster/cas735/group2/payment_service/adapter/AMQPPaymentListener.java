package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.business.PaymentService;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.PaymentActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
@Slf4j
public class AMQPPaymentListener implements PaymentActivity {

    private final PaymentService paymentService;

    @Autowired
    public AMQPPaymentListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void receivePaymentActivity(PaymentRequestDTO paymentRequestDTO) {
        paymentService.createOrderAndDetermineFines(paymentRequestDTO);
    }

    // TODO: Check if private works with @RabbitListener annotation
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "payment.activity.request", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "payment"))
    private void receive(String data, Channel channel, long tag) {
        PaymentRequestDTO paymentRequestDTO = convertToDTO(data);

        log.info("Received payment request: {} - with tag: {} - channel: {}", paymentRequestDTO, tag, channel);

        receivePaymentActivity(paymentRequestDTO);
    }

    private PaymentRequestDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, PaymentRequestDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
