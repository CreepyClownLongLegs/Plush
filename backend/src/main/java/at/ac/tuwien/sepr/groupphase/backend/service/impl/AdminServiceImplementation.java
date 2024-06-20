package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyAttributeDistributionMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.ProductCategoryMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.OrderItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttribute;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttributeDistribution;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyAttributeDistributionRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyAttributeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ProductCategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;

@Service
public class AdminServiceImplementation implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final PlushToyMapper plushToyMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final PlushToyAttributeDistributionMapper attributeDistributionMapper;

    private final UserRepository userRepository;
    private final PlushToyAttributeDistributionRepository attributeDistributionRepository;
    private final PlushToyAttributeRepository attributeRepository;
    private final OrderItemRepository orderItemRepository;

    public AdminServiceImplementation(PlushToyRepository plushToyRepository,
            PlushToyMapper plushToyMapper,
            ProductCategoryRepository productCategoryRepository,
            UserRepository userRepository,
            OrderItemRepository orderItemRepository,
            PlushToyAttributeRepository attributeRepository,
            PlushToyAttributeDistributionRepository attributeDistributionRepository,
            ProductCategoryMapper productCategoryMapper,
            PlushToyAttributeDistributionMapper attributeDistributionMapper) {
        this.plushToyRepository = plushToyRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.plushToyMapper = plushToyMapper;
        this.productCategoryMapper = productCategoryMapper;
        this.attributeDistributionMapper = attributeDistributionMapper;
        this.userRepository = userRepository;
        this.attributeDistributionRepository = attributeDistributionRepository;
        this.attributeRepository = attributeRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void deletePlushToy(@NonNull Long productId) throws NotFoundException {
        LOGGER.info("deletePlushToy {}", productId);
        PlushToy plushToy = plushToyRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("PlushToy not found"));
        List<OrderItem> orderItems = orderItemRepository.findByPlushToy(plushToy);
        for (OrderItem orderItem : orderItems) {
            orderItem.setPlushToy(null);
            orderItemRepository.save(orderItem);
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
    public PlushToyDetailDto addPlushToy(@NonNull PlushToyDetailDto toCreate) {
        LOGGER.info("addPlushToy");
        PlushToy plushToy = plushToyMapper.detailsDtoToEntity(toCreate);

        PlushToy created = plushToyRepository.save(plushToy);

        createUpdateDeletePlushToyAttributeDistributions(toCreate, created);
        LOGGER.trace("Generated PlushToy: {}", created.getId());
        return plushToyMapper.entityToDetailDto(created);
    }

    @Override
    public PlushToyDetailDto editPlushToy(@NonNull Long id, @NonNull PlushToyDetailDto plushToy)
            throws IllegalArgumentException, NotFoundException {
        LOGGER.info("editPlushToy id: {}, with {}", id, plushToy);
        if (plushToy.getId() != id) {
            throw new IllegalArgumentException("ID in path and body do not match");
        }
        PlushToy plushToyEntity = plushToyMapper.detailsDtoToEntity(plushToy);
        plushToyEntity.setId(id);

        createUpdateDeletePlushToyAttributeDistributions(plushToy, plushToyEntity);

        PlushToy edited = plushToyRepository.save(plushToyEntity);

        LOGGER.trace("Edited PlushToy: {}" + plushToyEntity.getId());
        return plushToyMapper.entityToDetailDto(edited);
    }

    @Override
    public ProductCategoryDto addProductCategory(@NonNull ProductCategory productCategoryDto) {
        LOGGER.info("addProductCategory");
        ProductCategory created = productCategoryRepository.save(productCategoryDto);
        LOGGER.trace("Generated ProductCategory: {}", created.getId());
        return productCategoryMapper.entityToDto(created);
    }

    @Override
    public void deleteProductCategory(Long productCategoryId) throws NotFoundException {
        LOGGER.info("deleteProductCategory {}", productCategoryId);
        if (!productCategoryRepository.existsById(productCategoryId)) {
            throw new NotFoundException("Product category not found");
        }
        productCategoryRepository.deleteById(productCategoryId);
    }

    @Override
    public List<ProductCategory> getAllProductCategories() {
        LOGGER.info("getAllProductCategories");
        return productCategoryRepository.findAll();
    }

    @Override
    public PlushToyDetailDto editPlushToyCategories(Long productId, List<Long> newProductCategoryIds) {
        LOGGER.info("editPlushToyCategories");

        PlushToy plushToy = plushToyRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Plush toy not found"));

        plushToy.getProductCategories().removeIf(category -> {
            if (!newProductCategoryIds.contains(category.getId())) {
                category.getPlushToys().remove(plushToy);
                productCategoryRepository.save(category);
                return true;
            }
            return false;
        });

        newProductCategoryIds.forEach(id -> {
            if (plushToy.getProductCategories().stream().noneMatch(category -> category.getId().equals(id))) {
                ProductCategory productCategory = productCategoryRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Product category not found"));
                productCategory.addPlushToy(plushToy);
                productCategoryRepository.save(productCategory);
            }
        });
        PlushToy edited = plushToyRepository.save(plushToy);
        LOGGER.trace("Categories edited of PlushToy: {}", edited.getId());
        return plushToyMapper.entityToDetailDto(edited);
    }

    @Override
    public List<User> getAllUsers() {
        LOGGER.info("getAllUsers");
        return userRepository.findAll();
    }

    public void updateUserAdminStatus(UserListDto userListDto) throws NotFoundException {
        LOGGER.info("updateUserAdminStatus {}", userListDto.getPublicKey());
        User user = userRepository.findUserByPublicKey(userListDto.getPublicKey())
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setAdmin(userListDto.isAdmin());
        userRepository.save(user);
    }

    private void createUpdateDeletePlushToyAttributeDistributions(PlushToyDetailDto dto, PlushToy entity) {
        LOGGER.info("Edited PlushToy Attributes: to {} from {}", dto, entity);
        PlushToy entityInDb = plushToyRepository.findById(entity.getId())
                .orElseThrow(() -> new NotFoundException("Plush toy not found"));

        // Delete old attributes
        entityInDb.getAttributeDistributions().forEach(attributeDistribution -> {
            if (dto.getAttributesDistributions().stream().noneMatch(
                    attributeDistributionDto -> attributeDistribution.getId()
                            .equals(attributeDistributionDto.getId()))) {
                attributeDistributionRepository.deleteById(attributeDistribution.getId());
            }
        });

        List<PlushToyAttributeDistribution> attributeDistributions = new ArrayList<>();
        dto.getAttributesDistributions().forEach(attributeDistributionDto -> {
            PlushToyAttributeDistribution dist = attributeDistributionMapper.dtoToEntity(attributeDistributionDto);

            PlushToyAttribute attribute = attributeRepository.findByName(dist.getAttribute().getName())
                    .orElse(null);
            if (attribute == null) {
                attribute = attributeRepository.save(dist.getAttribute());
            }
            if (attributeDistributionDto.getId() == null) {
                // Add new attributes
                dist.setAttribute(attribute);
                dist.setPlushToy(entity);
            } else {
                // Update attributes
                PlushToyAttributeDistribution distInDb = attributeDistributionRepository
                        .findById(attributeDistributionDto.getId())
                        .orElseThrow(() -> new NotFoundException("Attribute distribution not found"));
                dist.setAttribute(distInDb.getAttribute());
                dist.setPlushToy(distInDb.getPlushToy());
            }
            dist = attributeDistributionRepository.save(dist);
            attributeDistributions.add(dist);

        });
        entity.setAttributeDistributions(attributeDistributions);
    }

}
