package at.ac.tuwien.sepr.groupphase.backend.unittests;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.PlushToyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PlushToyServiceTest implements TestData {

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Autowired
    private PlushToyServiceImpl plushToyService;

    @Test
    public void givenNonExistingPlushToy_whenGetById_thenThrowNotFoundException() {
        Long nonExistingId = 999L;
        assertThrows(NotFoundException.class, () -> plushToyService.getById(nonExistingId));
    }

    @Test
    public void givenExistingPlushToy_whenGetById_thenReturnPlushToy() {
        // Arrange
        PlushToy plushy = new PlushToy();
        plushy.setName("TEST TIGER");
        plushy.setPrice(10.1);
        plushy.setTaxClass(10.0f);
        plushy.setWeight(1);
        plushy.setColor(Color.BLACK);
        plushy.setSize(Size.MEDIUM);
        plushy.setHp(100);  // Example value
        plushy.setStrength(50.0f);  // Example value
        plushy.setAverageRating(4.5);  // Example value

        PlushToy savedPlushToy = plushToyRepository.save(plushy);

        // Act
        PlushToy foundPlushToy = plushToyService.getById(savedPlushToy.getId());

        // Assert
        assertNotNull(foundPlushToy);
        assertEquals(savedPlushToy.getName(), foundPlushToy.getName());
        assertEquals(savedPlushToy.getPrice(), foundPlushToy.getPrice());
        assertEquals(savedPlushToy.getTaxClass(), foundPlushToy.getTaxClass());
        assertEquals(savedPlushToy.getWeight(), foundPlushToy.getWeight());
        assertEquals(savedPlushToy.getColor(), foundPlushToy.getColor());
        assertEquals(savedPlushToy.getSize(), foundPlushToy.getSize());
        assertEquals(savedPlushToy.getHp(), foundPlushToy.getHp());
        assertEquals(savedPlushToy.getStrength(), foundPlushToy.getStrength());
        assertEquals(savedPlushToy.getAverageRating(), foundPlushToy.getAverageRating());
    }
}
