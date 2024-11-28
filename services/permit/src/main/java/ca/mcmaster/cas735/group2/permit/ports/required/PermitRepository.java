package ca.mcmaster.cas735.group2.permit.ports.required;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitRepository extends JpaRepository<PermitData, String> {
    PermitData findByTransponderID(String transponderID);
    PermitData findByPlateNumber(String plateNumber);
}
