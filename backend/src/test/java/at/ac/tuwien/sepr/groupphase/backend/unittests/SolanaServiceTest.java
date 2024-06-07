package at.ac.tuwien.sepr.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.SolanaServiceTestData;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.common.PlushToySupplier;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.CreateSmartContractDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.PublicKeyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.UpdateSmartContractDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.NftPlushToyAttributeValueMapperImpl;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nft;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.NftRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SmartContractRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RestRequestService;
import at.ac.tuwien.sepr.groupphase.backend.service.SolanaService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.SolanaServiceImplementation;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(PowerMockRunner.class)
@ActiveProfiles("test")
@PrepareForTest({ SolanaServiceImplementation.class })
public class SolanaServiceTest implements TestData, PlushToyTestData, SolanaServiceTestData {

    @Autowired
    private SolanaService solanaService;

    @Autowired
    private PlushToyRepository plushToyRepository;

    @Autowired
    private SmartContractRepository smartContractRepository;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private PlushToySupplier plushToySupplier;

    @Autowired
    private NftPlushToyAttributeValueMapperImpl nftPlushToyAttributeValueMapper;

    @MockBean
    private RestRequestService requestService;

    @Value("${solana.pod.url}")
    private String solanaPodUrl;

    @BeforeEach
    public void beforeEach() {
        smartContractRepository.deleteAll();
        plushToyRepository.deleteAll();
        PublicKeyDto publicKeyDto = new PublicKeyDto(TEST_SMART_CONTRACT_PUBLIC_KEY);

        when(requestService.sendJsonRequest(any(WebClient.class),
                eq(HttpMethod.POST), contains("smart-contract"),
                any(CreateSmartContractDto.class), eq(PublicKeyDto.class)))
                .thenReturn(publicKeyDto);

        when(requestService.sendJsonRequest(any(WebClient.class), eq(HttpMethod.POST), any(String.class),
                any(UpdateSmartContractDto.class), eq(PublicKeyDto.class)))
                .thenReturn(publicKeyDto);
        when(requestService.sendJsonRequest(any(WebClient.class), eq(HttpMethod.POST), contains("nft"),
                any(PublicKeyDto.class), eq(PublicKeyDto.class)))
                .thenReturn(publicKeyDto);
    }

    @AfterEach
    public void afterEach() {
        smartContractRepository.deleteAll();
        nftRepository.deleteAll();
        plushToyRepository.deleteAll();
    }

    @Test
    public void createSmartContractWorksAsIntended() throws Exception {
        PlushToy plushy = plushToyRepository.save(plushToySupplier.getPlushie());

        SmartContract sm = solanaService.createSmartContract(plushy.getId());

        assertNotNull(sm);
        assertEquals(TEST_SMART_CONTRACT_PUBLIC_KEY, sm.getPublicKey());
        assertEquals(plushy.getId(), sm.getPlushToy().getId());
        assertEquals(plushy.getName(), sm.getName());
    }

    @Test
    public void mintNftWorksAsExpected() {
        PlushToy plushy = plushToyRepository.save(plushToySupplier.getPlushie());
        String receiverPublicKey = RESIVER_PUBLIC_KEY;

        Nft nft = solanaService.mintNft(plushy.getId(), receiverPublicKey);

        assertNotNull(nft);
        assertEquals(receiverPublicKey, nft.getOwnerId());
        List<SmartContract> smartContracts = smartContractRepository.findByPlushToyId(plushy.getId());
        assertEquals(1, smartContracts.size());

    }

    @Test
    public void createSmartContractThrowsNotFoundExceptionOnUnknowId() {
        assertThrows(NotFoundException.class, () -> solanaService.createSmartContract(-123123123L));
    }

    @Test
    public void mintNftThrowsNotFoundExceptionOnUnknowId() {
        assertThrows(NotFoundException.class, () -> solanaService.mintNft(-123123123L, RESIVER_PUBLIC_KEY));
    }
}
