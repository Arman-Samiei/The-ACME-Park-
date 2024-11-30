package ca.mcmaster.cas735.Group2.Voucher.ports;

import ca.mcmaster.cas735.Group2.Voucher.dto.SenML;

public interface Validate {

    void receive_transponder_info(SenML data);

}