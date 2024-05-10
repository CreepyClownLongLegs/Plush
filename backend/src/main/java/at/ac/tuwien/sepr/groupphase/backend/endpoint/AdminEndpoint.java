package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapper;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AdminService adminService;
    private final PlushToyMapper plushToyMapper;

    @Autowired
    public AdminEndpoint(AdminService adminService, PlushToyMapper plushToyMapper) {
        this.adminService = adminService;
        this.plushToyMapper = plushToyMapper;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/product/{id}")
    @Operation(summary = "Delete a product", security = @SecurityRequirement(name = "apiKey"), parameters = {
            @Parameter(name = "id", description = "The id of the product to delete", required = true, in = ParameterIn.PATH)
    })
    public void delete(@PathVariable("id") Long productId) {
        LOGGER.info("DELETE /api/v1/admin/product/{}", productId);
        adminService.deletePlushToy(productId);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/products")
    @Operation(summary = "Get all products", security = @SecurityRequirement(name = "apiKey"))
    public List<PlushToyListDto> getAllPlushtoys() {
        LOGGER.info("GET /api/v1/admin/products");
        return plushToyMapper.entityToListDto(adminService.getAllPlushToys());
    }

}
