package ca.mcmaster.cas735.group2.permit.ports.required;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PermitRepository extends JpaRepository<PermitData, String> {
    PermitData findByTransponderID(String transponderID);
    PermitData findByPlateNumber(String plateNumber);

    List<PermitData> findAllByExpirationTimeBefore(LocalDateTime time); // Query for expired permits

}
