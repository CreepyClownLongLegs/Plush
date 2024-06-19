package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.PlushToyService;

@Service
public class PlushToyServiceImpl implements PlushToyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;

    public PlushToyServiceImpl(PlushToyRepository plushToyRepository) {
        this.plushToyRepository = plushToyRepository;
    }

    @Override
    public PlushToy getById(long id) {
        LOGGER.debug("Find plush toy by id: {}", id);
        return plushToyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Could not find plush with id %d", id)));
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

}