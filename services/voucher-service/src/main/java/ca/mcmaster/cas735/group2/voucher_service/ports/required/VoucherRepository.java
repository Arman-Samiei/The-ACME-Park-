package ca.mcmaster.cas735.group2.voucher_service.ports.required;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoucherRepository extends JpaRepository<VoucherData, String> {
    VoucherData findByPlateNumber(String plateNumber);

    List<VoucherData> findAllByExpirationTimeBefore(LocalDateTime time); // Query for expired vouchers

}
