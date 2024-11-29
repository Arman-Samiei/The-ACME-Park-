package ca.mcmaster.cas735.group2.lot.ports.required;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LotRepository extends JpaRepository<LotData, String> {
    LotData findBySpotID(String spotID);
    LotData findByPlateNumber(String plateNumber);
    LotData findFirstByCustomerTypeAndSpotReservationStatusAndLotID(String customerType, String spotReservationStatus, String lotID);

}
