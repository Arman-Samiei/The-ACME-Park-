package ca.mcmaster.cas735.group2.payment_service.ports;

import ca.mcmaster.cas735.group2.payment_service.dto.NotifyFineDTO;

public interface NotifyFines {

    void sendFineNotification(NotifyFineDTO notifyFineDTO);
}
