package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;

@Service
public class AdminServiceImplementation implements AdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlushToyRepository plushToyRepository;

    public AdminServiceImplementation(PlushToyRepository plushToyRepository) {
        this.plushToyRepository = plushToyRepository;
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
}
