package at.ac.tuwien.sepr.groupphase.backend.endpoint;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapper;
import at.ac.tuwien.sepr.groupphase.backend.service.PlushToyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = "/api/v1/plush")
public class PlushEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PlushToyService plushToyService;
    private final PlushToyMapper plushToyMapper;

    @Autowired
    public PlushEndpoint(PlushToyService plushToyService, PlushToyMapper plushToyMapper) {
        this.plushToyService = plushToyService;
        this.plushToyMapper = plushToyMapper;
    }

    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get detailed information about a plush toy")
    public PlushToyDetailDto getById(@PathVariable(name = "id") Long id) {
        LOGGER.info("GET /api/v1/plush/{}", id);
        return plushToyMapper.entityToDetailDto(plushToyService.getById(id));
    }


}
