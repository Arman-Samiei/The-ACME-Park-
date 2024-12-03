package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotReleaseSpot;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import ca.mcmaster.cas735.group2.lot.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpotRelease implements LotReleaseSpot {

    private final LotRepository database;

    @Autowired
    public SpotRelease(LotRepository database) {
        this.database = database;
    }

    @Override
    public void releaseSpot(String plateNumber) {
        LotData lotData = database.findByPlateNumber(plateNumber);
        lotData.setPlateNumber(null);
        lotData.setSpotReservationStatus(Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED);
        database.saveAndFlush(lotData);
    }
}
