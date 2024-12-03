package ca.mcmaster.cas735.group2.voucher_service.business;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.ReleaseSpotRequest;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class VoucherCleanup {

    private final VoucherRepository database;

    private final ReleaseSpotRequest releaseSpotRequest;

    @Autowired
    public VoucherCleanup(VoucherRepository database, ReleaseSpotRequest releaseSpotRequest) {
        this.database = database;
        this.releaseSpotRequest = releaseSpotRequest;
    }

    @Scheduled(fixedRate = 86400000) // every 24 hours (day)
    public void removeExpiredVouchers() {
        LocalDateTime now = LocalDateTime.now();
        List<VoucherData> expiredVouchers = database.findAllByExpirationTimeBefore(now);

        for (VoucherData voucher : expiredVouchers)
            processExpiredPermit(voucher);
    }

    private void processExpiredPermit(VoucherData voucher) {
        String plateNumber = voucher.getPlateNumber();
        releaseSpotRequest.sendReleaseSpotRequest(plateNumber);
        log.debug("Voucher associated with {} plate number was expired!", plateNumber);
    }
}


