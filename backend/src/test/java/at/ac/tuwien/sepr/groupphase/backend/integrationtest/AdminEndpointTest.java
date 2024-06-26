package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.ProductCategoryTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.UserTestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyAttributeDistributionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyAttributeDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttribute;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttributeDistribution;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyAttributeDistributionRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyAttributeRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ProductCategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.SolanaServiceImplementation;
import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminEndpointTest implements PlushToyTestData, TestData, UserTestData, ProductCategoryTestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlushToyRepository plushToyRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlushToyAttributeDistributionRepository attributeDistributionRepository;
    @Autowired
    private PlushToyAttributeRepository attributeRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    private Supplier<ProductCategory> productCategorySupplier = () -> {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName(TEST_PRODUCT_CATEGORY_NAME);
        return productCategory;
    };

    private Supplier<PlushToy> plushySupplier = () -> {
        PlushToy plushy = new PlushToy();
        plushy.setName(TEST_PLUSHTOY_NAME);
        plushy.setPrice(TEST_PLUSHTOY_PRICE);
        plushy.setTaxClass(TEST_PLUSHTOY_TAX_CLASS);
        plushy.setWeight(TEST_PLUSHTOY_WEIGHT);
        plushy.setColor(Color.valueOf(TEST_PLUSHTOY_COLOR));
        plushy.setSize(Size.valueOf(TEST_PLUSHTOY_SIZE));
        plushy.setHp(TEST_PLUSHTOY_HP);
        plushy.setStrength(TEST_PLUSHTOY_STRENGTH);
        return plushy;
    };

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @Transactional
    public void beforeEach() {
        productCategoryRepository.deleteAll();
        plushToyRepository.deleteAll();
        userRepository.deleteAll();
        attributeRepository.deleteAll();

    }

    @MockBean
    private SolanaServiceImplementation solanaService;

    @Test
    public void deleteWorksAsInteded() throws Exception {
        PlushToy savePlushy = plushToyRepository.save(plushySupplier.get());

        MvcResult mvcResult = this.mockMvc.perform(delete(ADMIN_DELETE_PLUSH_TOY_URI, savePlushy.getId())
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void editWorksAsInteded() throws Exception {
        PlushToy savePlushy = plushToyRepository.save(plushySupplier.get());
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        ProductCategory x = productCategoryRepository.save(productCategory);
        String requestBody = """
                {
                    "id": %d,
                    "name": "%s",
                    "description": "%s",
                    "price": %f,
                    "taxClass": %f,
                    "weight": %f,
                    "size": "%s",
                    "color": "%s",
                    "hp": %d,
                    "imageUrl": %s,
                    "strength": %f,
                    "productCategories": [
                        {
                            "id": %d,
                            "name": "%s"
                        }
                    ]
                }
                """;

        requestBody = String.format(Locale.ROOT, requestBody, savePlushy.getId(), "new name",
                TEST_PLUSHTOY_DESCRIPTION,
                TEST_PLUSHTOY_PRICE, TEST_PLUSHTOY_TAX_CLASS,
                TEST_PLUSHTOY_WEIGHT, TEST_PLUSHTOY_SIZE, TEST_PLUSHTOY_COLOR, TEST_PLUSHTOY_HP, null,
                TEST_PLUSHTOY_STRENGTH, x.getId(), x.getName());

        MvcResult mvcResult = this.mockMvc.perform(put(ADMIN_DELETE_PLUSH_TOY_URI, savePlushy.getId())
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void editWorksAsIntededWithNewAttributes() throws Exception {
        PlushToy savePlushy = plushToyRepository.save(plushySupplier.get());
        String requestBody = """
                {
                    "id": %d,
                    "name": "%s",
                    "description": "%s",
                    "price": %f,
                    "taxClass": %f,
                    "weight": %f,
                    "size": "%s",
                    "color": "%s",
                    "hp": %d,
                    "imageUrl": "%s",
                    "strength": %f,
                    "attributesDistributions": [
                        {
                            "attribute": {
                                "name": "%s"
                            },
                            "quantityPercentage": %f,
                            "name": "%s"
                        },
                        {
                            "attribute": {
                                "name": "%s"
                            },
                            "quantityPercentage": %f,
                            "name": "%s"
                        }
                    ]
                }
                """;
        requestBody = String.format(Locale.ROOT, requestBody, savePlushy.getId(),
                "new name",
                TEST_PLUSHTOY_DESCRIPTION,
                TEST_PLUSHTOY_PRICE, TEST_PLUSHTOY_TAX_CLASS,
                TEST_PLUSHTOY_WEIGHT, TEST_PLUSHTOY_SIZE, TEST_PLUSHTOY_COLOR, TEST_PLUSHTOY_HP, null,
                TEST_PLUSHTOY_STRENGTH, TEST_PLUSHTOY_ATTRIBUTE_NAME,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME, TEST_PLUSHTOY_ATTRIBUTE_NAME,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE2,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME2);

        MvcResult mvcResult = this.mockMvc.perform(put(ADMIN_DELETE_PLUSH_TOY_URI, savePlushy.getId())
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        PlushToyDetailDto plushToyDetailDto = objectMapper.readValue(response.getContentAsString(),
                PlushToyDetailDto.class);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<PlushToyAttributeDistributionDto> attributesDistributions = plushToyDetailDto
                .getAttributesDistributions();
        assertEquals(2, attributesDistributions.size(), "There should be exactly 2 attribute distributions");

        PlushToyAttributeDistributionDto firstDistribution = attributesDistributions.get(0);
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME, firstDistribution.getName());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE, firstDistribution.getQuantityPercentage());
        assertNotNull(firstDistribution.getAttribute());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_NAME, firstDistribution.getAttribute().getName());

        PlushToyAttributeDto firstAttribute = attributesDistributions.get(0).getAttribute();
        PlushToyAttributeDistributionDto secondDistribution = attributesDistributions.get(1);
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME2, secondDistribution.getName());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE2, secondDistribution.getQuantityPercentage(), 0.01);
        assertNotNull(secondDistribution.getAttribute());
        assertEquals(firstAttribute.getId(), secondDistribution.getAttribute().getId());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_NAME, secondDistribution.getAttribute().getName());
    }

    @Test
    public void editWorksAsIntededWithUpdatedAndDeleteAttributes() throws Exception {
        PlushToyAttribute attribute = new PlushToyAttribute(TEST_PLUSHTOY_ATTRIBUTE_NAME);
        attribute = attributeRepository.save(attribute);
        PlushToyAttributeDistribution attributeDistribution = new PlushToyAttributeDistribution();
        attributeDistribution.setAttribute(attribute);
        attributeDistribution.setName(TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME);
        attributeDistribution.setQuantityPercentage(TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE);
        PlushToy savePlushy = plushToyRepository.save(plushySupplier.get());
        attributeDistribution.setPlushToy(savePlushy);
        attributeDistribution = attributeDistributionRepository.save(attributeDistribution);
        PlushToyAttributeDistribution attributeDistribution2 = new PlushToyAttributeDistribution();
        attributeDistribution2.setAttribute(attribute);
        attributeDistribution2.setName(TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME + "SHOULD NOT BE IN RESULT");
        attributeDistribution2.setQuantityPercentage(TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE);
        attributeDistribution2.setPlushToy(savePlushy);
        attributeDistribution2 = attributeDistributionRepository.save(attributeDistribution2);

        savePlushy.addAttributeDistribution(attributeDistribution);
        savePlushy.addAttributeDistribution(attributeDistribution2);
        savePlushy = plushToyRepository.save(savePlushy);
        String requestBody = """
                {
                    "id": %d,
                    "name": "%s",
                    "description": "%s",
                    "price": %f,
                    "taxClass": %f,
                    "weight": %f,
                    "size": "%s",
                    "color": "%s",
                    "hp": %d,
                    "imageUrl": "%s",
                    "strength": %f,
                    "attributesDistributions": [
                        {
                            "attribute": {
                                "name": "%s"
                            },
                            "quantityPercentage": %f,
                            "name": "%s",
                            "id": %d
                        },
                        {
                            "attribute": {
                                "name": "%s"
                            },
                            "quantityPercentage": %f,
                            "name": "%s"
                        }
                    ]
                }
                """;
        requestBody = String.format(Locale.ROOT, requestBody, savePlushy.getId(),
                "new name",
                TEST_PLUSHTOY_DESCRIPTION,
                TEST_PLUSHTOY_PRICE, TEST_PLUSHTOY_TAX_CLASS,
                TEST_PLUSHTOY_WEIGHT, TEST_PLUSHTOY_SIZE, TEST_PLUSHTOY_COLOR, TEST_PLUSHTOY_HP, null,
                TEST_PLUSHTOY_STRENGTH, TEST_PLUSHTOY_ATTRIBUTE_NAME,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE - 1,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME, attributeDistribution.getId(),
                TEST_PLUSHTOY_ATTRIBUTE_NAME,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE2,
                TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME2);

        MvcResult mvcResult = this.mockMvc.perform(put(ADMIN_DELETE_PLUSH_TOY_URI, savePlushy.getId())
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        PlushToyDetailDto plushToyDetailDto = objectMapper.readValue(response.getContentAsString(),
                PlushToyDetailDto.class);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        List<PlushToyAttributeDistributionDto> attributesDistributions = plushToyDetailDto
                .getAttributesDistributions();
        assertEquals(2, attributesDistributions.size(), "There should be exactly 2 attribute distributions");

        PlushToyAttributeDistributionDto firstDistribution = attributesDistributions.get(0);
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME, firstDistribution.getName());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE - 1, firstDistribution.getQuantityPercentage());
        assertNotNull(firstDistribution.getAttribute());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_NAME, firstDistribution.getAttribute().getName());

        PlushToyAttributeDistributionDto secondDistribution = attributesDistributions.get(1);
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME2, secondDistribution.getName());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE2, secondDistribution.getQuantityPercentage(), 0.01);
        assertNotNull(secondDistribution.getAttribute());
        PlushToyAttributeDto firstAttribute = attributesDistributions.get(0).getAttribute();
        assertEquals(firstAttribute.getId(), secondDistribution.getAttribute().getId());
        assertEquals(TEST_PLUSHTOY_ATTRIBUTE_NAME, secondDistribution.getAttribute().getName());
    }

    @Test
    public void editWorksAsIntededWithAttributes() throws Exception {
        PlushToy savePlushy = plushToyRepository.save(plushySupplier.get());
        PlushToyAttribute attribute = new PlushToyAttribute(TEST_PLUSHTOY_ATTRIBUTE_NAME);
        attribute = attributeRepository.save(attribute);
        PlushToyAttributeDistribution attributeDistribution = new PlushToyAttributeDistribution();
        attributeDistribution.setAttribute(attribute);
        attributeDistribution.setName(TEST_PLUSHTOY_ATTRIBUTE_DIST_NAME);
        attributeDistribution.setQuantityPercentage(TEST_PLUSHTOY_ATTRIBUTE_DIST_VALUE);
        attributeDistribution.setPlushToy(savePlushy);
        attributeDistribution = attributeDistributionRepository.save(attributeDistribution);
        savePlushy.addAttributeDistribution(attributeDistribution);
        savePlushy = plushToyRepository.save(savePlushy);
        String requestBody = """
                {
                    "id": %d,
                    "name": "%s",
                    "description": "%s",
                    "price": %f,
                    "taxClass": %f,
                    "weight": %f,
                    "size": "%s",
                    "color": "%s",
                    "hp": %d,
                    "imageUrl": %s,
                    "strength": %f,
                    "categories": %s
                }
                """;

        requestBody = String.format(Locale.ROOT, requestBody, -5, "new name", TEST_PLUSHTOY_DESCRIPTION,
                TEST_PLUSHTOY_PRICE, TEST_PLUSHTOY_TAX_CLASS,
                TEST_PLUSHTOY_WEIGHT, TEST_PLUSHTOY_SIZE, TEST_PLUSHTOY_COLOR, TEST_PLUSHTOY_HP, null,
                TEST_PLUSHTOY_STRENGTH, null);

        MvcResult mvcResult = this.mockMvc.perform(put(ADMIN_DELETE_PLUSH_TOY_URI, -5L)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void deleteWorksWithNotfoundId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(ADMIN_DELETE_PLUSH_TOY_URI, -12312314L)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void givenValidPlushToyAllFields_whenCreating_thenPlushToyPersisted() throws Exception {
        ProductCategory productCategory = productCategorySupplier.get();
        productCategory = productCategoryRepository.save(productCategory);

        String requestBody = """
                {
                    "name": "%s",
                    "description": "%s",
                    "size": "%s",
                    "color": "%s",
                    "price": %f,
                    "taxClass": %f,
                    "weight": %f,
                    "hp": %d,
                    "strength": %f
                }
                """;
        requestBody = String.format(Locale.ROOT, requestBody, TEST_PLUSHTOY_NAME, TEST_PLUSHTOY_DESCRIPTION,
                TEST_PLUSHTOY_SIZE,
                TEST_PLUSHTOY_COLOR, TEST_PLUSHTOY_PRICE, TEST_PLUSHTOY_TAX_CLASS, TEST_PLUSHTOY_WEIGHT,
                TEST_PLUSHTOY_HP, TEST_PLUSHTOY_STRENGTH);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/product")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        PlushToyDetailDto plushToyDetailsDto = objectMapper.readValue(response.getContentAsString(),
                PlushToyDetailDto.class);
        assertThat(plushToyDetailsDto)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", TEST_PLUSHTOY_NAME)
                .hasFieldOrPropertyWithValue("description", TEST_PLUSHTOY_DESCRIPTION)
                .hasFieldOrPropertyWithValue("price", TEST_PLUSHTOY_PRICE)
                .hasFieldOrPropertyWithValue("taxClass", TEST_PLUSHTOY_TAX_CLASS)
                .hasFieldOrPropertyWithValue("weight", TEST_PLUSHTOY_WEIGHT)
                .hasFieldOrPropertyWithValue("hp", TEST_PLUSHTOY_HP)
                .hasFieldOrPropertyWithValue("strength", TEST_PLUSHTOY_STRENGTH);

        assertEquals(plushToyDetailsDto.getColor().toString(), TEST_PLUSHTOY_COLOR);
        assertEquals(plushToyDetailsDto.getSize().toString(), TEST_PLUSHTOY_SIZE);
        assertThat(plushToyDetailsDto.getId()).isGreaterThan(0L);
    }

    @Test
    public void givenMalformedInput_WhenCreating_thenBadRequest() throws Exception {
        String requestBody = """
                {}
                """;

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/product")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }

    @Test
    public void givenValidCategoryAllFields_whenCreatingCategory_thenCategoryPersisted() throws Exception {
        String requestBody = """
                {
                    "name": "%s"
                }
                """;
        requestBody = String.format(requestBody, TEST_PRODUCT_CATEGORY_NAME + "1");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/categories")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        assertThat(response.getContentAsString()).contains(TEST_PRODUCT_CATEGORY_NAME + "1");

        // Get categories
        mvcResult = mockMvc.perform(get("/api/v1/admin/categories")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();

        response = mvcResult.getResponse();
        assertThat(response.getContentAsString()).contains(TEST_PRODUCT_CATEGORY_NAME + "1");
    }

    @Test
    public void givenMalformedCategoryInput_WhenCreatingCategory_thenBadRequest() throws Exception {
        String requestBody = """
                {}
                """;

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/categories")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }

    @Test
    public void givenValidCategory_WhenDeletingCategory_thenCategoryDeleted() throws Exception {
        ProductCategory productCategory = productCategoryRepository.save(productCategorySupplier.get());

        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/admin/categories/" + productCategory.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertThat(productCategoryRepository.findById(productCategory.getId())).isEmpty();
    }

    @WithMockUser(roles = "ADMIN")
    public void givenUsersExist_whenGetAllUsers_thenAllUsersAreReturned() throws Exception {
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

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/admin/allUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        UserListDto[] users = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                UserListDto[].class);
        assertEquals(2, users.length);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenValidUserListDto_whenUpdateUserAdminStatus_thenUserAdminStatusIsUpdated() throws Exception {
        User user = new User();
        user.setPublicKey(TEST_PUBKEY);
        user.setAdmin(false);
        userRepository.save(user);

        UserListDto userListDto = new UserListDto();
        userListDto.setPublicKey(TEST_PUBKEY);
        userListDto.setAdmin(true);

        String requestBody = new ObjectMapper().writeValueAsString(userListDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        Optional<User> updatedUser = userRepository.findUserByPublicKey(TEST_PUBKEY);
        assertTrue(updatedUser.isPresent());
        assertTrue(updatedUser.get().isAdmin());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenInvalidPublicKey_whenUpdateUserAdminStatus_thenNotFoundExceptionIsThrown() throws Exception {
        UserListDto userListDto = new UserListDto();
        userListDto.setPublicKey(TEST_NONEXISTENT_PUBKEY);
        userListDto.setAdmin(true);

        String requestBody = new ObjectMapper().writeValueAsString(userListDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenValidProductIdAndCategoryIds_whenSetCategories_thenCategoriesSet() throws Exception {
        PlushToy plushToy = plushToyRepository.save(plushySupplier.get());
        ProductCategory category1 = productCategoryRepository.save(productCategorySupplier.get());
        ProductCategory category2 = productCategoryRepository.save(productCategorySupplier.get());
        List<Long> categoryIds = List.of(category1.getId(), category2.getId());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/product/" + plushToy.getId() + "/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(categoryIds)))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        PlushToyDetailDto plushToyDetailDto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PlushToyDetailDto.class);
        assertNotNull(plushToyDetailDto);
        assertEquals(2, plushToyDetailDto.getProductCategories().size());
        assertTrue(plushToyDetailDto.getProductCategories().stream().anyMatch(category -> category.getId().equals(category1.getId())));
        assertTrue(plushToyDetailDto.getProductCategories().stream().anyMatch(category -> category.getId().equals(category2.getId())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenInvalidProductId_whenSetCategories_thenReturnNotFound() throws Exception {
        List<Long> categoryIds = List.of(1L, 2L);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/admin/product/9999/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(categoryIds)))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenUsersExist_whenGetAllUsers_thenReturnAllUsers() throws Exception {
        User user1 = new User();
        user1.setPublicKey(TEST_PUBKEY);
        user1.setFirstname("Thomas");
        user1.setLastname("Schmidt");
        userRepository.save(user1);

        User user2 = new User();
        user2.setPublicKey(TEST_PUBKEY_2);
        user2.setFirstname("Franz");
        user2.setLastname("Hintermeier");
        userRepository.save(user2);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        UserListDto[] users = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UserListDto[].class);
        assertEquals(2, users.length);
    }
}
