package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotStatisticsRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@RestController
@Tag(name = "Get some statistics about the lots")
@RequestMapping("/api/lot")
public class LotStatisticsRequestAdapter {

    private final LotStatisticsRequest lotStatisticsRequest;

    @Autowired
    public LotStatisticsRequestAdapter(LotStatisticsRequest lotStatisticsRequest) {
        this.lotStatisticsRequest = lotStatisticsRequest;
    }

    @GetMapping("/occupied")
    @Operation(description = "Getting all occupied spots")
    public List<LotData> getOccupiedSpots() {
        return lotStatisticsRequest.getOccupiedSpots();
    }

    @GetMapping("/free")
    @Operation(description = "Get all free spots")
    public List<LotData> getFreeSpots() {
        return lotStatisticsRequest.getFreeSpots();
    }

    @GetMapping("/all")
    @Operation(description = "Get all spots (occupied + free)")
    public List<LotData> getAllSpots() {
        return lotStatisticsRequest.getAllSpots();
    }
}