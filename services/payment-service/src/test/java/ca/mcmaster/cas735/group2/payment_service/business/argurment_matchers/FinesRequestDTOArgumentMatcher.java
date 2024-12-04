package ca.mcmaster.cas735.group2.payment_service.business.argurment_matchers;

import ca.mcmaster.cas735.group2.payment_service.dto.FinesRequestDTO;
import org.mockito.ArgumentMatcher;

public class FinesRequestDTOArgumentMatcher implements ArgumentMatcher<FinesRequestDTO> {

    private final FinesRequestDTO left;

    public FinesRequestDTOArgumentMatcher(FinesRequestDTO left) {
        this.left = left;
    }

    @Override
    public boolean matches(FinesRequestDTO right) {
        return left.plateNumber().equals(right.plateNumber());
    }
}
