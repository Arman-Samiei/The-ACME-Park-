package ca.mcmaster.cas735.group2.permit.business;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitRepository;
import ca.mcmaster.cas735.group2.permit.ports.required.ReleaseSpotRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PermitCleanup {

    private final PermitRepository database;

    private final ReleaseSpotRequest releaseSpotRequest;

    @Autowired
    public PermitCleanup(PermitRepository database, ReleaseSpotRequest releaseSpotRequest) {
        this.database = database;
        this.releaseSpotRequest = releaseSpotRequest;
    }

    @Scheduled(fixedRate = 86400000) // every 24 hours (day)
    public void removeExpiredPermits() {
        LocalDateTime now = LocalDateTime.now();
        List<PermitData> expiredPermits = database.findAllByExpirationTimeBefore(now);

        for (PermitData permit : expiredPermits)
            processExpiredPermit(permit);
    }

    private void processExpiredPermit(PermitData permitData) {
        String plateNumber = permitData.getPlateNumber();
        releaseSpotRequest.sendReleaseSpotRequest(plateNumber);
        log.debug("Permit associated with {} plate number was expired!", plateNumber);
    }
}


