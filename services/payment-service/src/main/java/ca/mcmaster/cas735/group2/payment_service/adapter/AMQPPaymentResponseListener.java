package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.business.PaymentService;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.PaymentResponse;
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
public class AMQPPaymentResponseListener implements PaymentResponse {

    private final PaymentService paymentService;

    @Autowired
    public AMQPPaymentResponseListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void handlePaymentResponse(PaymentResponseDTO paymentResponseDTO) {
        paymentService.comfirmOrderAndRoute(paymentResponseDTO);
    }

    // TODO: Check if private works with @RabbitListener annotation
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "payment.activity.response", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "payment"))
    private void receive(String data, Channel channel, long tag) {
        PaymentResponseDTO paymentResponseDTO = convertToDTO(data);

        log.info("Received payment response: {} - with tag: {} - channel: {}", paymentResponseDTO, tag, channel);

        handlePaymentResponse(paymentResponseDTO);
    }

    private PaymentResponseDTO convertToDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, PaymentResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
