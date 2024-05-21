package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;

public interface PlushToyService {

    /**
     * Get plush toy information.
     *
     * @return information about a plush toy for detailed view
     */
    PlushToy getById(long id);

}
