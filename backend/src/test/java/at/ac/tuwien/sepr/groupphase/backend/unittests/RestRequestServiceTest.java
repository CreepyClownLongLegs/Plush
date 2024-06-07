package at.ac.tuwien.sepr.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import at.ac.tuwien.sepr.groupphase.backend.service.impl.RestRequestServiceImpl;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RestRequestServiceTest {

    private WebClient webClient;

    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    private WebClient.RequestHeadersSpec requestHeadersSpec;

    private RestRequestServiceImpl restRequestService;

    @BeforeEach
    public void setUp() {
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        restRequestService = new RestRequestServiceImpl();
        when(webClient.method(any(HttpMethod.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(Object.class))).thenReturn(requestHeadersSpec);

    }

    @Test
    public void testSendJsonRequestWithBody() {
        String url = "http://localhost";
        String requestBody = "request";
        String responseBody = "response";

        when(requestHeadersSpec.exchangeToMono(any(Function.class))).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<String>> function = invocation.getArgument(0);
            ClientResponse clientResponse = mock(ClientResponse.class);

            when(clientResponse.statusCode()).thenReturn(HttpStatus.OK);
            when(clientResponse.bodyToMono(eq(String.class))).thenReturn(Mono.just(responseBody));
            return function.apply(clientResponse);
        });

        String result = restRequestService.sendJsonRequest(webClient, HttpMethod.GET, url, requestBody, String.class);

        assertEquals(responseBody, result);
    }

    @Test
    public void testSendJsonRequestWithoutBody() {
        String url = "http://localhost";
        String responseBody = "response";
        when(requestBodyUriSpec.exchangeToMono(any(Function.class))).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<String>> function = invocation.getArgument(0);
            ClientResponse clientResponse = mock(ClientResponse.class);

            when(clientResponse.statusCode()).thenReturn(HttpStatus.OK);
            when(clientResponse.bodyToMono(eq(String.class))).thenReturn(Mono.just(responseBody));
            return function.apply(clientResponse);
        });
        String result = restRequestService.sendJsonRequest(webClient, HttpMethod.GET, url, String.class);

        assertEquals(responseBody, result);
    }
}
