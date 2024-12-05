package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.dto.LotOccupationStatusUpdateData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotOccupationStatusUpdate;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import ca.mcmaster.cas735.group2.lot.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class LotOccupation implements LotOccupationStatusUpdate {
    private final LotRepository database;

    @Autowired
    public LotOccupation(LotRepository database) {
        this.database = database;
    }

    @Override
    public void updateSpotOccupationStatus(LotOccupationStatusUpdateData lotOccupationStatusUpdateData) {
        LotData lotData = database.findBySpotID(lotOccupationStatusUpdateData.getSpotID());
        String customerType = lotData.getCustomerType();
        lotData.setIsSpotOccupied(lotOccupationStatusUpdateData.getIsSpotOccupied());
        if (!lotData.getHasVoucher() && Objects.equals(customerType, Constants.VISITOR_CUSTOMER_TYPE))
            lotData.setSpotReservationStatus(Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED);
        database.saveAndFlush(lotData);
    }
}
