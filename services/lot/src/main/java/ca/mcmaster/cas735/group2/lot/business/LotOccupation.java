package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.dto.LotOccupationStatusUpdateData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotOccupationStatusUpdate;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        lotData.setIsSpotOccupied(lotOccupationStatusUpdateData.getIsSpotOccupied());
        database.saveAndFlush(lotData);
    }
}
