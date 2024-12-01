package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesIssuanceData;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesIssuance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Issue Fines")
@RequestMapping("/api/fines")
public class FinesIssuanceAdapter {
    private final FinesIssuance finesIssuance;

    @Autowired
    public FinesIssuanceAdapter(FinesIssuance finesIssuance) {
        this.finesIssuance = finesIssuance;
    }

    @PostMapping(value = "/")
    @Operation(description = "Issuing the Fines")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody FinesIssuanceData data) {
        finesIssuance.issueFine(data);
    }


}
