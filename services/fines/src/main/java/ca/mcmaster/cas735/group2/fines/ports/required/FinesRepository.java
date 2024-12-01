package ca.mcmaster.cas735.group2.fines.ports.required;


import ca.mcmaster.cas735.group2.fines.business.entities.FinesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FinesRepository extends JpaRepository<FinesData, Long> {
    @Query("SELECT SUM(f.amount) FROM FinesData f WHERE f.plateNumber = :plateNumber")
    Double findTotalAmountByPlateNumber(@Param("plateNumber") String plateNumber);

}
