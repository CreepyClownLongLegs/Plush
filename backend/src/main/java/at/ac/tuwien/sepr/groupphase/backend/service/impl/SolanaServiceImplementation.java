package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.CreateSmartContractDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.PublicKeyDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.NftPlushToyAttributeValueMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Nft;
import at.ac.tuwien.sepr.groupphase.backend.entity.NftPlushToyAttributeValue;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttribute;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToyAttributeDistribution;
import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.NftPlushToyAttributeValueRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.NftRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SmartContractRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RestRequestService;
import at.ac.tuwien.sepr.groupphase.backend.service.SolanaService;
import jakarta.annotation.PostConstruct;

/**
 * Implementation of the SolanaService.
 * Works with WebClient see https://www.baeldung.com/spring-5-webclient for more
 * information.
 */
@Service
public class SolanaServiceImplementation implements SolanaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Value("${solana.pod.url}")
    private String solanaPodUrl;
    private String baseUrl = "https://testMyPlushy.com/";
    private String createSmartContractUrl;
    private Function<String, String> getMintNftUrl = (publicKey) -> solanaPodUrl + "nft/" + publicKey;
    private Function<String, String> updateMintUrl = (publicKey) -> solanaPodUrl + "smart-contract/" + publicKey;
    private Function<String, String> getTokenInfoUrl = (smartContractPublicKey) -> baseUrl + "t/"
            + smartContractPublicKey;
    private SmartContractRepository smartContractRepository;
    private NftRepository nftRepository;
    private PlushToyRepository plushToyRepository;
    private NftPlushToyAttributeValueRepository nftPlushToyAttributeValueRepository;
    private WebClient client;
    private NftPlushToyAttributeValueMapper nftPlushToyAttributeValueMapper;
    private RestRequestService restRequestService;

    @Autowired
    public SolanaServiceImplementation(SmartContractRepository smartContractRepository,
            PlushToyRepository plushToyRepository, NftRepository nftRepository,
            NftPlushToyAttributeValueRepository nftPlushToyAttributeValueRepository,
            NftPlushToyAttributeValueMapper nftPlushToyAttributeValueMapper,
            RestRequestService restRequestService) {
        this.smartContractRepository = smartContractRepository;
        this.plushToyRepository = plushToyRepository;
        this.nftRepository = nftRepository;
        this.nftPlushToyAttributeValueRepository = nftPlushToyAttributeValueRepository;
        this.nftPlushToyAttributeValueMapper = nftPlushToyAttributeValueMapper;
        this.restRequestService = restRequestService;
    }

    @PostConstruct
    public void init() {
        this.createSmartContractUrl = solanaPodUrl + "smart-contract";
        this.client = WebClient.builder()
                .baseUrl(solanaPodUrl)
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public SmartContract createSmartContract(@NonNull Long plushToyId) {
        LOGGER.info("Creating a new smart contract");
        PlushToy plushToy = plushToyRepository.findById(plushToyId)
                .orElseThrow(() -> new NotFoundException("PlushToy not found"));
        CreateSmartContractDto createSmartContractDto = new CreateSmartContractDto(plushToy.getName(),
                baseUrl);
        PublicKeyDto response = restRequestService.sendJsonRequest(client, HttpMethod.POST,
                createSmartContractUrl,
                createSmartContractDto,
                PublicKeyDto.class);
        SmartContract smartContract = new SmartContract();
        smartContract.setPublicKey(response.getPublicKey());
        smartContract.setName(plushToy.getName());
        smartContract.setPlushToy(plushToy);
        return smartContractRepository.save(smartContract);
    }

    @Override
    public Nft mintNft(@NonNull Long plushToyId, @NonNull String receiverPublicKey) {
        LOGGER.info("Minting a new NFT for {}", plushToyId);
        PlushToy plushToy = plushToyRepository.findById(plushToyId)
                .orElseThrow(() -> new NotFoundException("PlushToy not found"));

        SmartContract smartContract = createSmartContract(plushToyId);

        List<NftPlushToyAttributeValue> attributeValues = new ArrayList<>();

        Random random = new Random();

        for (PlushToyAttribute attribute : plushToy.getPlushToyAttributes()) {
            NftPlushToyAttributeValue attributeValue = new NftPlushToyAttributeValue();
            attributeValue.setAttribute(attribute);
            List<PlushToyAttributeDistribution> distributions = attribute.getDistributions();
            float choosen = random.nextFloat(100);
            distributions.sort(
                    (a, b) -> Float.compare(a.getQuantityPercentage(), b.getQuantityPercentage()));
            float segStart = 0;

            for (PlushToyAttributeDistribution dis : distributions) {
                if (choosen >= segStart && choosen < segStart + dis.getQuantityPercentage()) {
                    attributeValue.setValue(dis.getName());
                    attributeValues.add(attributeValue);
                    LOGGER.info("Choosing random Attribute {}", dis);
                    break;
                }
                segStart += dis.getQuantityPercentage();
            }
        }

        PublicKeyDto request = new PublicKeyDto(receiverPublicKey);
        PublicKeyDto response = restRequestService.sendJsonRequest(client, HttpMethod.POST,
                getMintNftUrl.apply(smartContract.getPublicKey()), request,
                PublicKeyDto.class);

        Nft nft = new Nft();

        nft.setPlushToy(plushToy);
        nft.setTimestamp(LocalDateTime.now());
        nft.setOwnerId(receiverPublicKey);
        nft.setPublicKey(smartContract.getPublicKey());
        nft.setDescription(plushToy.getDescription());
        nft.setName("NFT for " + plushToy.getName());
        nft = nftRepository.save(nft);
        for (NftPlushToyAttributeValue nftAttributeValues : attributeValues) {
            nftAttributeValues.setNft(nft);
            nftPlushToyAttributeValueRepository.save(nftAttributeValues);
            nft.getAttributes().add(nftAttributeValues);
        }

        return nft;
    }
}
