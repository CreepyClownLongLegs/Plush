package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.ProductCategoryTestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PlushToyListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ProductCategoryDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SearchPlushToyDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.ProductCategory;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ProductCategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PlushToyEndpointTest implements PlushToyTestData, ProductCategoryTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Supplier<PlushToy> plushySupplier = () -> {
        PlushToy plushToy = new PlushToy();
        plushToy.setName("Test Plush Toy");
        plushToy.setPrice(10.0);
        plushToy.setTaxClass(10.0f);
        plushToy.setWeight(1.0);
        plushToy.setColor(Color.BLACK);
        plushToy.setSize(Size.MEDIUM);
        plushToy.setHp(100);
        plushToy.setStrength(50.0f);
        return plushToy;
    };

    private Supplier<ProductCategory> productCategorySupplier = () -> {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("Test Category");
        return productCategory;
    };

    @BeforeEach
    public void beforeEach() {
        plushToyRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    public void givenValidPlushToyId_whenGetById_thenReturnPlushToyDetails() throws Exception {
        PlushToy plushToy = plushToyRepository.save(plushySupplier.get());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/plush/" + plushToy.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        PlushToyDetailDto plushToyDetailDto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PlushToyDetailDto.class);
        assertNotNull(plushToyDetailDto);
        assertEquals(plushToy.getId(), plushToyDetailDto.getId());
    }

    @Test
    public void givenInvalidPlushToyId_whenGetById_thenReturnNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/plush/9999")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void givenPlushToysExist_whenGetAllPlushtoys_thenReturnAllPlushtoys() throws Exception {
        plushToyRepository.save(plushySupplier.get());
        plushToyRepository.save(plushySupplier.get());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/plush")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        PlushToyListDto[] plushToys = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PlushToyListDto[].class);
        assertTrue(plushToys.length >= 2);
    }

    @Test
    public void givenSearchParams_whenSearch_thenReturnMatchingPlushtoys() throws Exception {
        PlushToy plushToy = plushToyRepository.save(plushySupplier.get());
        SearchPlushToyDto searchParams = new SearchPlushToyDto();
        searchParams.setName(plushToy.getName());

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/plush/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(searchParams)))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        PlushToyListDto[] plushToys = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), PlushToyListDto[].class);
        assertTrue(plushToys.length > 0);
        assertEquals(plushToy.getName(), plushToys[0].getName());
    }

    @Test
    public void givenCategoriesExist_whenGetAllCategories_thenReturnAllCategories() throws Exception {
        productCategoryRepository.save(productCategorySupplier.get());
        productCategoryRepository.save(productCategorySupplier.get());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/plush/categories")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        ProductCategoryDto[] categories = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), ProductCategoryDto[].class);
        assertTrue(categories.length >= 2);
    }
}
