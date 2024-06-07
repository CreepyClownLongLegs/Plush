package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import at.ac.tuwien.sepr.groupphase.backend.service.RestRequestService;
import reactor.core.publisher.Mono;

@Service
public class RestRequestServiceImpl implements RestRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public <T> T sendJsonRequest(WebClient client, HttpMethod method, String url, Object requestBody,
            Class<T> responseType) {
        LOGGER.info("Sending request {} to {} with {}", method, url, requestBody);
        UriSpec<RequestBodySpec> uriSpec = client.method(method);
        RequestBodySpec bodySpec = uriSpec.uri(url);
        RequestHeadersSpec<?> headersSpec = null;
        if (requestBody != null) {
            headersSpec = bodySpec.bodyValue(requestBody);
        } else {
            headersSpec = bodySpec;
        }
        LOGGER.debug("Request {}", headersSpec);
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

    public <T> T sendJsonRequest(WebClient client, HttpMethod method, String url, Class<T> responseType) {
        return sendJsonRequest(client, method, url, null, responseType);
    }
}
