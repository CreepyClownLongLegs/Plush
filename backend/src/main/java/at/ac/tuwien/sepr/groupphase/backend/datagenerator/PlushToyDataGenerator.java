package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import jakarta.annotation.PostConstruct;

@Profile("generateData")
@Component
public class PlushToyDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_TO_GENERATE = 5;
    private static final String TEST_NAME = "TEST TIGER";

    private final PlushToyRepository plushToyRepository;

    public PlushToyDataGenerator(PlushToyRepository plushToyRepository) {
        this.plushToyRepository = plushToyRepository;
    }

    @PostConstruct
    private void generate() {
        if (plushToyRepository.findAll().size() > 0) {
            LOGGER.debug("plushToy already generated");
        } else {
            LOGGER.debug("generating {} plushToy entries", NUMBER_TO_GENERATE);
            for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
                PlushToy plushy = new PlushToy();
                plushy.setName(TEST_NAME + " " + i);
                plushy.setPrice(10.0);
                plushy.setTaxClass(10.0f);
                plushy.setWeight(i);
                plushy.setColor(Color.BLACK);
                plushy.setSize(Size.MEDIUM);
                plushy.setHp(100);
                plushy.setStrength(i % 5);
                LOGGER.debug("saving plushToy {}", plushy);
                plushToyRepository.save(plushy);
                LOGGER.info("created plushToy with ID: {} , {}", plushy.getId(), plushy);
            }
        }
    }

}
