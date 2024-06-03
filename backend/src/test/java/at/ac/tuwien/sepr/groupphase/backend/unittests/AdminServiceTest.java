package at.ac.tuwien.sepr.groupphase.backend.unittests;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.PlushToyMapperImpl;
import at.ac.tuwien.sepr.groupphase.backend.entity.*;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdminServiceTest implements TestData, PlushToyTestData, UserTestData {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Autowired
    private UserRepository userRepository;

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

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

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

    @Test
    public void givenUsersExist_whenGetAllUsers_thenAllUsersAreReturned() {
        User user1 = new User();
        user1.setPublicKey(TEST_PUBKEY);
        user1.setFirstname("firstname1");
        user1.setLastname("lastname1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setPublicKey(TEST_PUBKEY_2);
        user2.setFirstname("firstname2");
        user2.setLastname("lastname2");
        userRepository.save(user2);

        List<User> users = adminService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    public void givenValidUserListDto_whenUpdateUserAdminStatus_thenUserAdminStatusIsUpdated() {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        user.setAdmin(false);
        userRepository.save(user);

        UserListDto userListDto = new UserListDto();
        userListDto.setPublicKey(TEST_PUBKEY);
        userListDto.setAdmin(true);

        adminService.updateUserAdminStatus(userListDto);

        Optional<User> updatedUser = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertTrue(updatedUser.isPresent());
        assertTrue(updatedUser.get().isAdmin());
    }

    @Test
    public void givenInvalidPublicKey_whenUpdateUserAdminStatus_thenNotFoundExceptionIsThrown() {
        UserListDto userListDto = new UserListDto();
        userListDto.setPublicKey(TEST_NONEXISTENT_PUBKEY);
        userListDto.setAdmin(true);

        assertThrows(NotFoundException.class, () -> adminService.updateUserAdminStatus(userListDto));
    }

}
