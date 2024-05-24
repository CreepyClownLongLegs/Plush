package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana.CreateSmartContractDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.SmartContract;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlushToyRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SmartContractRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.SolanaService;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

/**
 * Implementation of the SolanaService.
 * Works with WebClient see https://www.baeldung.com/spring-5-webclient for more
 * information.
 * 
 */
@Service
public class SolanaServiceImplementation implements SolanaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Value("${solana.pod.url}")
    private String solanaPodUrl;
    private String createSmartContractUrl;

    private SmartContractRepository smartContractRepository;
    private PlushToyRepository plushToyRepository;
    private WebClient client;

    @Autowired
    public SolanaServiceImplementation(SmartContractRepository smartContractRepository,
            PlushToyRepository plushToyRepository) {
        this.smartContractRepository = smartContractRepository;
        this.plushToyRepository = plushToyRepository;
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
        CreateSmartContractDto response = sendJsonRequest(HttpMethod.POST, createSmartContractUrl,
                CreateSmartContractDto.class);
        PlushToy plushToy = plushToyRepository.findById(plushToyId).get();
        SmartContract smartContract = new SmartContract();
        smartContract.setPublicKey(response.getPublicKey());
        smartContract.setName(plushToy.getName() + " smart contract");
        smartContract.setPlushToy(plushToy);
        SmartContract sm = smartContractRepository.save(smartContract);
        return sm;
    }

    private <T> T sendJsonRequest(HttpMethod method, String url, Object requestBody, Class<T> responseType) {
        LOGGER.info("Sending request {} to {} with {}", method, url, requestBody);
        UriSpec<RequestBodySpec> uriSpec = client.method(method);
        RequestBodySpec bodySpec = uriSpec.uri(url);
        RequestHeadersSpec<?> headersSpec = null;
        if (requestBody != null) {
            headersSpec = bodySpec.bodyValue(requestBody);
        } else {
            headersSpec = bodySpec;
        }
        ResponseSpec responseSpec = headersSpec
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve();
        LOGGER.debug("Request {}", responseSpec);
        Mono<T> responseT = headersSpec.exchangeToMono(response -> {
            LOGGER.info("Response {}", response);
            if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(responseType);
            } else if (response.statusCode().is4xxClientError()) {
                LOGGER.warn("Solana Client Error {}", response);
                return response.createException()
                        .flatMap(Mono::error);
            } else {
                LOGGER.error("Solana Pod Error {}", response);
                return response.createException()
                        .flatMap(Mono::error);
            }
        });
        T result = responseT.block();
        LOGGER.info("Mapped Response to {}", result);
        return result;
    }

    private <T> T sendJsonRequest(HttpMethod method, String url, Class<T> responseType) {
        return sendJsonRequest(method, url, null, responseType);
    }
}
