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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryCreationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.ProductCategoryMapper;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;
import at.ac.tuwien.sepr.groupphase.backend.service.SolanaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import jakarta.annotation.security.PermitAll;


@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AdminService adminService;
    private final PlushToyMapper plushToyMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final SolanaService solanaService;

    @Autowired
    public AdminEndpoint(AdminService adminService, PlushToyMapper plushToyMapper,
                         ProductCategoryMapper productCategoryMapper, SolanaService solanaService) {
        this.adminService = adminService;
        this.plushToyMapper = plushToyMapper;
        this.productCategoryMapper = productCategoryMapper;
        this.solanaService = solanaService;
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

    @PermitAll
    @GetMapping(value = "/allProducts")
    @Operation(summary = "Get all products")
    public List<PlushToyListDto> getAllPlushtoys() {
        LOGGER.info("GET /api/v1/admin/allProducts");
        return plushToyMapper.entityToListDto(adminService.getAllPlushToys());
    }

    @PermitAll
    @PostMapping(value = "/products")
    @Operation(summary = "Search for products")
    public List<PlushToyListDto> search(@RequestBody SearchPlushToyDto searchParams) {
        LOGGER.info("POST /api/v1/admin/products with body: {}", searchParams);
        return plushToyMapper.entityToListDto(adminService.search(searchParams));
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/product")
    @Operation(summary = "Create a new product", security = @SecurityRequirement(name = "apiKey"))
    public PlushToyDetailDto create(@Valid @RequestBody PlushToyDetailDto plushToyDetailDto) {
        LOGGER.info("Creating new product. body: {}", plushToyDetailDto);
        PlushToyDetailDto res = adminService.addPlushToy(plushToyMapper.detailsDtoToEntity(plushToyDetailDto));

        solanaService.createSmartContract(res.getId());

        if (plushToyDetailDto.getProductCategories() != null) {
            return adminService.editPlushToyCategories(res.getId(), productCategoryMapper.productCategoryDtoListToIdList(plushToyDetailDto.getProductCategories()));

        } else {
            return res;
        }
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/product/{id}")
    @Operation(summary = "Edit an existing product", security = @SecurityRequirement(name = "apiKey"))
    public PlushToyDetailDto update(@PathVariable(name = "id") Long id, @Valid @RequestBody PlushToyDetailDto plushToyDetailDto) {
        LOGGER.info("Editing an existing product. body: {}", plushToyDetailDto);

        if (plushToyDetailDto.getProductCategories() != null) {
            adminService.editPlushToyCategories(id, productCategoryMapper.productCategoryDtoListToIdList(plushToyDetailDto.getProductCategories()));
        }

        return adminService.editPlushToy(id, plushToyDetailDto);

    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/categories")
    @Operation(summary = "Create a new category", security = @SecurityRequirement(name = "apiKey"))
    public ProductCategoryDto createCategory(@Valid @RequestBody ProductCategoryCreationDto category) {
        LOGGER.info("Creating new category. body: {}", category);
        return adminService.addProductCategory(productCategoryMapper.creationDtoToEntity(category));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/categories")
    @Operation(summary = "Get all categories", security = @SecurityRequirement(name = "apiKey"))
    public List<ProductCategoryDto> getAllCategories() {
        LOGGER.info("GET /api/v1/admin/categories");
        return productCategoryMapper.entityListToDtoList(adminService.getAllProductCategories());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/product/{id}/categories")
    @Operation(summary = "Add categories to a product", security = @SecurityRequirement(name = "apiKey"), parameters = {
        @Parameter(name = "id", description = "The id of the product to update", required = true, in = ParameterIn.PATH)
    })
    public PlushToyDetailDto setCategories(@PathVariable("id") Long productId, @RequestBody List<Long> categoryIds) {
        LOGGER.info("Adding categories to product with id {}. body: {}", productId, categoryIds);
        return adminService.editPlushToyCategories(productId, categoryIds);
    }

}
