package ca.mcmaster.cas735.group2.voucher_service.business.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;


@Entity @Data public class VoucherData {
    @Id
    private String plateNumber;

    private String lotID;

    private String spotID;

    private String status;

    private LocalDateTime expirationTime;
}