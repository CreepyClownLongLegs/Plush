package at.ac.tuwien.sepr.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.function.Supplier;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdminServiceTest implements TestData, PlushToyTestData {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PlushToyRepository plushToyRepository;

    private Supplier<PlushToy> plushySupplier = () -> {
        PlushToy plushy = new PlushToy();
        plushy.setName(TEST_PLUSHTOY_NAME);
        plushy.setPrice(TEST_PLUSHTOY_PRICE);
        plushy.setTaxClass(TEST_PLUSHTOY_TAX_CLASS);
        plushy.setWeight(TEST_PLUSHTOY_WEIGHT);
        plushy.setColor(Color.valueOf(TEST_PLUSHTOY_COLOR));
        plushy.setSize(Size.valueOf(TEST_PLUSHTOY_SIZE));
        return plushy;
    };
    @Autowired
    private PlushToyMapperImpl plushToyMapperImpl;

    @Test
    public void deleteWorksAsIntededForValidId() {
        PlushToy plushy = plushToyRepository.save(plushySupplier.get());

        adminService.deletePlushToy(plushy.getId());

        Optional<PlushToy> result = plushToyRepository.findById(plushy.getId());
        assertFalse(result.isPresent());
    }

    @Test
    public void editPlushToyWorksAsIntended() {
        PlushToy plushy = plushToyRepository.save(plushySupplier.get());

        PlushToyDetailDto plushyDetailsDto = plushToyMapperImpl.entityToDetailDto(plushy);
        plushyDetailsDto.setName("New Name");
        plushyDetailsDto.setPrice(200.0);

        PlushToyDetailDto updatedPlushy = adminService.editPlushToy(plushy.getId(), plushyDetailsDto);

        assertEquals("New Name", updatedPlushy.getName());
        assertEquals(200.0, updatedPlushy.getPrice());
    }

    @Test
    public void editThrowsIllegalArgumentExceptionWithInvalidID() {
        PlushToyDetailDto plushyDetailsDto = new PlushToyDetailDto();
        plushyDetailsDto.setName("New Name");
        plushyDetailsDto.setPrice(200.0);
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.editPlushToy(-5L, plushyDetailsDto);
        });
    }

    @Test
    public void throwsNotFoundExceptionOnUnknowId() {
        assertThrows(NotFoundException.class, () -> adminService.deletePlushToy(-123123123L));
    }

}
