package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.lang.NonNull;

import java.util.List;

public interface AdminService {

    /**
     * Delete a plushtoy by id.
     *
     * @param productId the id of the product
     * @throws NotFoundException if the product does not exist
     */
    void deletePlushToy(@NonNull Long productId) throws NotFoundException;

    /**
     * Get all plushtoys.
     *
     * @return a list of all plushtoys
     */
    List<PlushToy> getAllPlushToys();

    /**
     * Get all plushtoys.
     *
     * @return a list of all plushtoys
     */
    List<PlushToy> search(SearchPlushToyDto searchParams);

    /**
     * Add a new product.
     *
     * @param plushToy the product to add
     * @return the ID of the added product
     */
    PlushToyDetailDto addPlushToy(@NonNull PlushToy plushToy);

    /**
     * Add a new product category.
     *
     * @param productCategory the product category to add
     * @return the added product category
     */
    ProductCategoryDto addProductCategory(@NonNull ProductCategory productCategory);

    /**
     * Get all product categories.
     *
     * @return a list of all product categories
     */
    List<ProductCategory> getAllProductCategories();

    /**
     * Updates an existing product.
     *
     * @param id       the ID of the product to edit
     * @param plushToy the product to edit
     * @return the updated product
     * @throws IllegalArgumentException if the given ID does not match the ID in the product
     * @throws NotFoundException        if the product does not exist
     */
    PlushToyDetailDto editPlushToy(@NonNull Long id, @NonNull PlushToyDetailDto plushToy) throws IllegalArgumentException, NotFoundException;

    /**
     * Edits the categories of a plush toy.
     *
     * @param productId             The ID of the plush toy to edit.
     * @param newProductCategoryIds A list of IDs representing the new categories to be associated with the plush toy.
     * @return A PlushToyDetailDto object representing the updated plush toy.
     * @throws NotFoundException if the plush toy or any of the categories does not exist.
     */
    PlushToyDetailDto editPlushToyCategories(Long productId, List<Long> newProductCategoryIds) throws NotFoundException;

    /**
     * Retrieves all users in the system.
     *
     * @return a list of User objects representing all users in the system
     */
    List<User> getAllUsers();

    /**
     * Updates the admin status of a user.
     *
     * @param userListDto is the user to be updated
     * @throws NotFoundException if the user cannot be found
     */
    void updateUserAdminStatus(UserListDto userListDto) throws NotFoundException;
}