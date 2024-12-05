package ca.mcmaster.cas735.group2.voucher_service.business.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "voucher")
public class VoucherData {
    @Id
    private String plateNumber;

    @Column(name = "LOT_ID")
    private String lotID;
    @Column(name = "SPOT_ID")
    private String spotID;

    private String status;

    private LocalDateTime expirationTime;
}