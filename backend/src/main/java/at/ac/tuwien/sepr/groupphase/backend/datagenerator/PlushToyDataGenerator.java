package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttribute;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttributeDistribution;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyAttributeDistributionRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyAttributeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import jakarta.annotation.PostConstruct;

@Component
@Profile({ "generateData", "dev-clean" })
public class PlushToyDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_TO_GENERATE = 5;

    private final PlushToyRepository plushToyRepository;
    private final PlushToyAttributeDistributionRepository attributeDistributionRepository;
    private final PlushToyAttributeRepository attributeRepository;

    public PlushToyDataGenerator(PlushToyRepository plushToyRepository,
            PlushToyAttributeDistributionRepository attributeDistributionRepository,
            PlushToyAttributeRepository attributeRepository) {
        this.plushToyRepository = plushToyRepository;
        this.attributeDistributionRepository = attributeDistributionRepository;
        this.attributeRepository = attributeRepository;
    }

    private static final String[] imageUrls = {
            "https://th.bing.com/th/id/OIP.oKqusaK-O2O2QGxKogbHNwHaHa?rs=1&pid=ImgDetMain",
            "https://cdn.pixabay.com/photo/2016/04/19/15/23/teddy-1338895_960_720.jpg",
            "https://cdn.pixabay.com/photo/2024/06/19/07/33/animal-8839382_640.jpg",
            "https://cdn.pixabay.com/photo/2024/01/27/10/23/shark-8535643_640.jpg",
            "https://cdn.pixabay.com/photo/2014/08/26/19/20/rabbit-428323_640.jpg" };

    @PostConstruct
    private void generate() {
        if (plushToyRepository.findAll().size() > 0) {
            LOGGER.debug("plushToy already generated");
        } else {
            LOGGER.debug("generating {} plushToy entries", NUMBER_TO_GENERATE);
            // with name test tiger
            PlushToyAttribute attribute1 = new PlushToyAttribute("strength");
            PlushToyAttribute attribute2 = new PlushToyAttribute("lightning");
            PlushToyAttribute attribute3 = new PlushToyAttribute("poison");
            attribute1 = attributeRepository.save(attribute1);
            attribute2 = attributeRepository.save(attribute2);
            attribute3 = attributeRepository.save(attribute3);

            for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
                PlushToy plushy = new PlushToy();
                PlushToyAttributeDistribution distribution = new PlushToyAttributeDistribution();
                PlushToyAttributeDistribution distribution2 = null;
                switch (i % 5) {
                    case 2:
                        plushy.setName("Baron");
                        plushy.setDescription("Cute lil gent");
                        distribution.setAttribute(attribute1);
                        distribution.setQuantityPercentage(100f);
                        distribution.setName("cute strength");
                        break;
                    case 3:
                        plushy.setName("Nilo");
                        plushy.setDescription("Doing a lot of exercise");
                        distribution.setAttribute(attribute1);
                        distribution.setQuantityPercentage(100f);
                        distribution.setName("moderat strength");
                        break;
                    case 4:
                        plushy.setName("Atlantifang");
                        plushy.setDescription("Hates water");
                        distribution.setAttribute(attribute2);
                        distribution.setQuantityPercentage(100f);
                        distribution.setName("red lightning");

                        distribution2 = new PlushToyAttributeDistribution();
                        distribution2.setAttribute(attribute3);
                        distribution2.setQuantityPercentage(100f);
                        distribution2.setName("moderate");

                        break;
                    case 5:
                        plushy.setName("Rocky");
                        plushy.setDescription("the beast");
                        distribution.setAttribute(attribute1);
                        distribution.setQuantityPercentage(100f);
                        distribution.setName("super strong");
                        break;
                    default:
                        plushy.setName("Quaki");
                        plushy.setDescription("Feisty lil fella");
                        distribution.setAttribute(attribute3);
                        distribution.setQuantityPercentage(100f);
                        distribution.setName("sleepy poison");
                        break;
                }
                plushy.setPrice(0.01);
                plushy.setTaxClass(20.0f);
                plushy.setImageUrl(imageUrls[i % imageUrls.length]);
                plushy.setWeight(i);
                plushy.setColor(Color.values()[i % Color.values().length]);
                plushy.setSize(Size.values()[i % Size.values().length]);
                plushy.setHp(100 + i * 15);
                plushy.setStrength(i + 1 % 6);
                LOGGER.debug("saving plushToy {}", plushy);
                plushy = plushToyRepository.save(plushy);
                distribution.setPlushToy(plushy);
                attributeDistributionRepository.save(distribution);
                if (distribution2 != null) {
                    distribution2.setPlushToy(plushy);
                    attributeDistributionRepository.save(distribution2);
                }
                LOGGER.info("created plushToy with ID: {} , {}", plushy.getId(), plushy);
            }

        }
    }

}
