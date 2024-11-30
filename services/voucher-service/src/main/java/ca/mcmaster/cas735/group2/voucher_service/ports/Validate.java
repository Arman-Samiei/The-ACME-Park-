package ca.mcmaster.cas735.group2.voucher_service.ports;

import ca.mcmaster.cas735.group2.voucher_service.dto.SenML;

public interface Validate {

    void receive_transponder_info(SenML data);

}