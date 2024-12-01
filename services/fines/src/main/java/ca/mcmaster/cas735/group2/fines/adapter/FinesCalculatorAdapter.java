package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.ports.provided.FinesCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FinesCalculatorAdapter {
    private final FinesCalculator finesCalculator;

    @Autowired
    public FinesCalculatorAdapter(FinesCalculator finesCalculator) {
        this.finesCalculator = finesCalculator;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.fines.calculator.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "fines.payment.request"))
    public void listen(String data) {
        log.debug("Receiving issuance request {}", data);
        finesCalculator.calculateTotalFine(data);

    }

}


