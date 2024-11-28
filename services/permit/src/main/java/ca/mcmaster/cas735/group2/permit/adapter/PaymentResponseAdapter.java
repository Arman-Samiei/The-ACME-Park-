package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PaymentResponseData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentResponseAdapter {
    private final PaymentResponse paymentResponse;

    @Autowired
    PaymentResponseAdapter(PaymentResponse paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.permit.payment.response.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "permit.payment.response"))
    public void listen(String data) {
        log.debug("Receiving payment response {}", data);
        paymentResponse.receivePaymentResponse(translate(data));

    }


    private PaymentResponseData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, PaymentResponseData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
