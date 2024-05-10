package at.ac.tuwien.sepr.groupphase.backend.service;

import java.util.List;

import org.springframework.lang.NonNull;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
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

}
