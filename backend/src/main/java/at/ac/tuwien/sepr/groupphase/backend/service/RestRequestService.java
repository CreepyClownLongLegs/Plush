package at.ac.tuwien.sepr.groupphase.backend.service;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public interface RestRequestService {

    /**
     * Sends a JSON request to the given URL with the given method and request body.
     *
     * @param <T>          the type of the response
     * @param client       the WebClient to use
     * @param method       the HTTP method to use
     * @param url          the URL to send the request to
     * @param requestBody  the request body
     * @param responseType the class type of the response
     * @return the response
     */
    <T> T sendJsonRequest(WebClient client, HttpMethod method, String url, Object requestBody, Class<T> responseType);

    /**
     * Sends a JSON request to the given URL with the given method.
     *
     * @param <T>          the type of the response
     * @param client       the WebClient to use
     * @param method       the HTTP method to use
     * @param url          the URL to send the request to
     * @param responseType the class type of the
     * @return the response
     */
    <T> T sendJsonRequest(WebClient client, HttpMethod method, String url, Class<T> responseType);
}
