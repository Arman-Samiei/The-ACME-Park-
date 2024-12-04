package ca.mcmaster.cas735.group2.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
public class VoucherIssuanceRequest {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange}")
    private String exchange;

    @Autowired
    public VoucherIssuanceRequest(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean shouldContinue = true;

            while (shouldContinue) {
                String plateNumber = askForInput(reader, "Enter the plate number:");
                String lotID = askForInput(reader, "Enter the lot ID:");

                VoucherIssuanceRequestDTO data = new VoucherIssuanceRequestDTO(plateNumber, lotID);
                log.info("Requesting voucher issuance for plate number: {} and lot ID: {}", plateNumber, lotID);

                rabbitTemplate.convertAndSend(exchange, "voucher.issue.request", translate(data));

                String continueResponse = askForInput(reader, "Do you want to continue? (yes/no)");
                shouldContinue = !"no".equalsIgnoreCase(continueResponse);
            }

        } catch (IOException e) {
            log.error("Error reading input: {}", e.getMessage());
        }
    }

    private String askForInput(BufferedReader reader, String prompt) {
        try {
            System.out.println(prompt);
            return reader.readLine();
        } catch (IOException e) {
            log.error("Error reading user input: {}", e.getMessage());
            return "";
        }
    }

    private String translate(VoucherIssuanceRequestDTO voucherIssuanceRequestDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(voucherIssuanceRequestDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }
}
