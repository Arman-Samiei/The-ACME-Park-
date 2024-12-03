package ca.mcmaster.cas735.group2.fines.ports.required;


import ca.mcmaster.cas735.group2.fines.business.entities.FinesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FinesRepository extends JpaRepository<FinesData, Long> {

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinesData f WHERE f.plateNumber = :plateNumber")
    Double findTotalAmountByPlateNumber(@Param("plateNumber") String plateNumber);

    @Transactional
    void deleteByPlateNumber(String plateNumber);
}
