package ca.mcmaster.cas735.group2.voucher_service.business;

import ca.mcmaster.cas735.group2.voucher_service.dto.SenML;
import ca.mcmaster.cas735.group2.voucher_service.ports.Issue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class VoucherValidation implements Validatement, Issue {

    private static final Set<String> registered = new HashSet<>();

    @Override
    public void register(String voucherID) {
        registered.add(voucherID);
    }

    @Override
    public void unregister(String voucherID) {
        registered.remove(voucherID);
    }

    @Override
    public void receive_transponder_info(SenML data) {
        if (registered.contains(data.getId())) {
            log.info("Voucher accepted");
        } else {
            log.error("Voucher Not Accepted");
        }
    }
}
