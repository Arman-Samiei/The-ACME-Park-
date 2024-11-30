package ca.mcmaster.cas735.group2.permit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PermitApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermitApplication.class, args);
    }
}
