package ca.mcmaster.cas735.group2.member;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class MemberApplication implements ApplicationRunner {

    private final PermitIssuanceRequest permitIssuanceRequest;

    @Autowired
    public MemberApplication(PermitIssuanceRequest permitIssuanceRequest) {
        this.permitIssuanceRequest = permitIssuanceRequest;
    }

    @Override
    public void run(ApplicationArguments args) {
        permitIssuanceRequest.sendData();
    }

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }
}
