package ca.mcmaster.cas735.group2.fines.ports.required;


import ca.mcmaster.cas735.group2.fines.business.entities.FinesData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinesRepository extends JpaRepository<FinesData, Long> {

}
