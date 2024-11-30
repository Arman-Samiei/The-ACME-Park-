package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotStatisticsRequest;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import ca.mcmaster.cas735.group2.lot.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LotStatistics implements LotStatisticsRequest {

    private final LotRepository database;

    @Autowired
    public LotStatistics(LotRepository database) {
        this.database = database;
    }

    public List<LotData> getAllSpots() {
        return database.findAll();
    }

    public List<LotData> getOccupiedSpots() {
        return database.findAll().stream()
                .filter(spot -> Constants.OCCUPIED.equalsIgnoreCase(spot.getSpotOccupationStatus()))
                .collect(Collectors.toList());
    }

    public List<LotData> getFreeSpots() {
        return database.findAll().stream()
                .filter(spot -> Constants.FREE.equalsIgnoreCase(spot.getSpotOccupationStatus()))
                .collect(Collectors.toList());
    }
}
