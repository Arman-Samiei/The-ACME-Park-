package ca.mcmaster.cas735.group2.member;

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
import java.util.Random;

@Service
@Slf4j
public class PermitIssuanceRequest {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange}")
    private String exchange;

    @Autowired
    public PermitIssuanceRequest(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                String initialTransponderLetter = generateRandomInitialLetter();
                String transponderID = generateRandomTransponderID(initialTransponderLetter);
                String plateNumber = askForInput(reader, "Enter the plate number:");
                String lotID = askForInput(reader, "Enter the lot ID:");
                String firstName = askForInput(reader, "Enter the first name:");
                String lastName = askForInput(reader, "Enter the last name:");
                String employeeID = askForInput(reader, "Enter the employee ID:");
                String memberPaymentType = askForInput(reader, "Enter the member payment type (payslip, bank):");
                String ccNumber = askForInput(reader, "Enter the credit card number:");
                String ccExpiry = askForInput(reader, "Enter the credit card expiry date (MM/YY):");
                String ccCVC = askForInput(reader, "Enter the credit card CVC:");
                Integer monthsPurchased = Integer.parseInt(askForInput(reader, "Enter the number of months:"));

                PermitIssuanceRequestDTO data = PermitIssuanceRequestDTO.builder()
                        .transponderID(transponderID)
                        .plateNumber(plateNumber)
                        .firstName(firstName)
                        .lastName(lastName)
                        .employeeID(employeeID)
                        .lotID(lotID)
                        .memberPaymentType(memberPaymentType)
                        .ccNumber(ccNumber)
                        .ccExpiry(ccExpiry)
                        .ccCVC(ccCVC)
                        .monthsPurchased(monthsPurchased)
                        .build();

                log.info("Requesting permit issuance for: {}", data.getTransponderID());

                rabbitTemplate.convertAndSend(exchange, "permit.issue.request", translate(data));

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

    private String generateRandomInitialLetter() {
        Random random = new Random();
        String generatedLetter = "s";
        int randomNumber = random.nextInt(3);
        switch (randomNumber) {
            case 0 -> generatedLetter = "s";
            case 1 -> generatedLetter = "f";
            case 2 -> generatedLetter = "m";
        }
        return generatedLetter;
    }

    // Used as a simulation for scanning transponder
    private String generateRandomTransponderID(String initialLetter) {
        Random random = new Random();
        StringBuilder digits = new StringBuilder(initialLetter);
        for (int i = 0; i < 10; i++) {
            digits.append(random.nextInt(10));
        }
        return digits.toString();
    }

    private String translate(PermitIssuanceRequestDTO permitIssuanceRequestDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(permitIssuanceRequestDTO);
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
