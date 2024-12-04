package ca.mcmaster.cas735.group2.admin;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class AdminApplication implements ApplicationRunner {

    private final VoucherIssuanceRequest voucherIssuanceRequest;

    @Autowired
    public AdminApplication(VoucherIssuanceRequest voucherIssuanceRequest) {
        this.voucherIssuanceRequest = voucherIssuanceRequest;
    }

    @Override
    public void run(ApplicationArguments args) {
        voucherIssuanceRequest.sendData();
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
