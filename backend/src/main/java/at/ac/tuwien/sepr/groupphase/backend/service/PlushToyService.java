package at.ac.tuwien.sepr.groupphase.backend.service;

import java.util.List;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;

public interface PlushToyService {

    /**
     * Get plush toy information.
     *
     * @return information about a plush toy for detailed view
     */
    PlushToy getById(long id);

    /**
     * Get all plush toys.
     *
     * @return list of all plush toys
     */
    List<PlushToy> getAllPlushToys();

    /**
     * Search plushtoys by either category or name.
     *
     * @return a list of all plushtoys
     */
    List<PlushToy> search(SearchPlushToyDto searchParams);

    /**
     * Get all product categories.
     *
     * @return a list of all product categories
     */
    List<ProductCategory> getAllProductCategories();

}
