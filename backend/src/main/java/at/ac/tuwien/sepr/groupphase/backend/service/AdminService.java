package at.ac.tuwien.sepr.groupphase.backend.service;

import java.util.List;

import org.springframework.lang.NonNull;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

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
     * Add product categories to a product.
     *
     * @param productId the id of the product
     * @param productCategoryIds the ids of the product categories
     * @return the updated product
     * @throws NotFoundException if the product does not exist
     */
    PlushToyDetailDto addCategoriesToProduct(@NonNull Long productId, @NonNull List<Long> productCategoryIds) throws NotFoundException;

}
