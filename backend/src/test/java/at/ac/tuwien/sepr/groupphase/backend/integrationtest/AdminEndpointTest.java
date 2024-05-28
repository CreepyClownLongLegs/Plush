package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Locale;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ProductCategoryRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SmartContractRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.SolanaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminEndpointTest implements PlushToyTestData, TestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

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
    public void beforeEach() {
        productCategoryRepository.deleteAll();
        plushToyRepository.deleteAll();

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(solanaService.createSmartContract(argumentCaptor.capture())).thenAnswer(invocation -> {
            Long capturedId = argumentCaptor.getValue();
            SmartContract s = new SmartContract();
            s.setName(TEST_SMART_CONTRACT_NAME);
            s.setPublicKey(TEST_SMART_CONTRACT_PUBLIC_KEY);
            s.setPlushToy(plushToyRepository.getReferenceById(capturedId));
            return smartContractRepository.save(s);
        });
    }

    @MockBean
    private SolanaService solanaService;

    @Autowired
    private SmartContractRepository smartContractRepository;

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

        requestBody = String.format(Locale.ROOT, requestBody, savePlushy.getId(), "new name", TEST_PLUSHTOY_DESCRIPTION,
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
    public void editWorksWithNotfoundId() throws Exception {
        plushToyRepository.save(plushySupplier.get());
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
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
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
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    }
}
