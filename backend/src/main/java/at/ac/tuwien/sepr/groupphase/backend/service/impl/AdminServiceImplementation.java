package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.ProductCategoryMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.OrderItem;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderItemRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ProductCategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class AdminServiceImplementation implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final PlushToyMapper plushToyMapper;
    private final ProductCategoryMapper productCategoryMapper;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public AdminServiceImplementation(PlushToyRepository plushToyRepository, PlushToyMapper plushToyMapper, ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper,
                                      OrderItemRepository orderItemRepository, UserRepository userRepository) {
        this.plushToyRepository = plushToyRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.plushToyMapper = plushToyMapper;
        this.productCategoryMapper = productCategoryMapper;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deletePlushToy(@NonNull Long productId) throws NotFoundException {
        LOGGER.info("deletePlushToy {}", productId);
        PlushToy plushToy = plushToyRepository.findById(productId).orElseThrow(() -> new NotFoundException("PlushToy not found"));
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
    public PlushToyDetailDto addPlushToy(@NonNull PlushToy toCreate) {
        LOGGER.info("addPlushToy");

        PlushToy created = plushToyRepository.save(toCreate);
        LOGGER.trace("Generated PlushToy: {}", created.getId());
        return plushToyMapper.entityToDetailDto(created);
    }

    @Override
    public PlushToyDetailDto editPlushToy(@NonNull Long id, PlushToyDetailDto plushToy) throws IllegalArgumentException, NotFoundException {
        LOGGER.info("editPlushToy");
        if (plushToy.getId() != id) {
            throw new IllegalArgumentException("ID in path and body do not match");
        }
        PlushToy plushToyEntity = plushToyMapper.detailsDtoToEntity(plushToy);
        plushToyRepository.findById(id).orElseThrow(() -> new NotFoundException("Plush toy not found"));
        plushToyEntity.setId(id);
        PlushToy edited = plushToyRepository.save(plushToyEntity);
        LOGGER.trace("Edited PlushToy: " + plushToyEntity.getId());
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
}
