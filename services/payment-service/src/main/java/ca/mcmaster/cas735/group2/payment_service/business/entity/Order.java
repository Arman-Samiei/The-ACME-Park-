package ca.mcmaster.cas735.group2.payment_service.business.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    private String id;
    private String staffId;
    private String plateNumber;
    private String ccNumber;
    private String ccExpiry;
    private String ccCVC;
    @Column(name = "LOT_ID")
    private String lotID;
    private String spotID;
    private double amount;
    private PaymentType paymentType;
    private boolean isPaid;
}
