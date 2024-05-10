package at.ac.tuwien.sepr.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;

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
        return plushy;
    };

    @BeforeEach
    public void beforeEach() {
        plushToyRepository.deleteAll();

    }

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
    public void deleteWorksWithNotfoundId() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(ADMIN_DELETE_PLUSH_TOY_URI, -12312314L)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

}
