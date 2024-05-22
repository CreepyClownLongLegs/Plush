package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.ProductCategoryMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ProductCategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;

@Service
public class AdminServiceImplementation implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final PlushToyMapper plushToyMapper;
    private final ProductCategoryMapper productCategoryMapper;

    public AdminServiceImplementation(PlushToyRepository plushToyRepository,
                                      PlushToyMapper plushToyMapper,
                                      ProductCategoryRepository productCategoryRepository,
                                      ProductCategoryMapper productCategoryMapper) {
        this.plushToyRepository = plushToyRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.plushToyMapper = plushToyMapper;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public void deletePlushToy(@NonNull Long productId) throws NotFoundException {
        LOGGER.info("deletePlushToy {}", productId);
        if (!plushToyRepository.existsById(productId)) {
            throw new NotFoundException("Plush toy not found");
        }
        plushToyRepository.deleteById(productId);
    }

    @Override
    public List<PlushToy> getAllPlushToys() {
        LOGGER.info("getAllPlushToys");
        return plushToyRepository.findAll();
    }

    @Override
    public List<PlushToy> search(SearchPlushToyDto searchParams) {
        LOGGER.info("searchPlushToys");
        String name = searchParams.getName();

        if (name != null && name.isEmpty()) {
            name = null;
        }

        return plushToyRepository.searchPlushToys(name);
    }

    @Override
    public PlushToyDetailDto addPlushToy(PlushToy toCreate) {
        LOGGER.info("addPlushToy");

        PlushToy created = plushToyRepository.save(toCreate);
        LOGGER.trace("Generated PlushToy: " + created.getId());
        return plushToyMapper.entityToDetailDto(created);        
    }

    @Override
    public ProductCategoryDto addProductCategory(ProductCategory productCategoryDto) {
        LOGGER.info("addProductCategory");
        ProductCategory created = productCategoryRepository.save(productCategoryDto);
        LOGGER.trace("Generated ProductCategory: " + created.getId());
        return productCategoryMapper.entityToDto(created);
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        LOGGER.info("getAllProductCategories");
        return productCategoryRepository.findAll();
    }

    @Override
    public PlushToyDetailDto addCategoriesToProduct(Long productId, List<Long> productCategoryIds) throws NotFoundException {
        LOGGER.info("addCategoriesToProduct");
        PlushToy plushToy = plushToyRepository.findById(productId).orElseThrow(() -> new NotFoundException("Plush toy not found"));

        for (Long id : productCategoryIds) {
            // Ignore categories that are already set
            if (plushToy.getProductCategories().stream().anyMatch(pc -> pc.getId().equals(id))) {
                continue;
            }
            ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Product category not found"));
            productCategory.addPlushToy(plushToy);
            productCategoryRepository.save(productCategory);
        }

        plushToyRepository.save(plushToy);
        return plushToyMapper.entityToDetailDto(plushToyRepository.findById(productId).get());
    }
}

