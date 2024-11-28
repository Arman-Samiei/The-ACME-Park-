package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.business.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDataRepository extends JpaRepository<Order, String> {

    public Optional<Order> findById(String id);
    public boolean existsById(String id);
}
