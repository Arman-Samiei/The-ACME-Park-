package ca.mcmaster.cas735.group2.payment_service.business.entity;

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
    private String gateId;
    private double amount;
    private PaymentType paymentType;
    private boolean isPaid;
}
