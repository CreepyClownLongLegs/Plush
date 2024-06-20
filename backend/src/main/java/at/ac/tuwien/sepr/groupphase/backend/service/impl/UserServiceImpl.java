package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Order;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import jakarta.transaction.Transactional;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private static final String SOLANA_RPC_URL = "https://api.devnet.solana.com";
    private final OkHttpClient httpClient = new OkHttpClient();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public void deleteUser(@NonNull String publicKey) throws NotFoundException {
        LOGGER.info("deleteUser {}", publicKey);
        var user = userRepository.findUserByPublicKey(publicKey);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteByPublicKey(publicKey);
    }

    @Override
    @Transactional
    public User findUserByPublicKey(@NonNull String publicKey) throws NotFoundException {
        LOGGER.info("findUserByPublicKey{}", publicKey);
        var user = userRepository.findUserByPublicKey(publicKey);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return user.get();
    }

    @Override
    @Transactional
    public User updateUser(@NonNull String publicKey, UserDetailDto userDetailDto) throws NotFoundException {
        LOGGER.info("updateUser {}", publicKey);
        var userOptional = userRepository.findUserByPublicKey(publicKey);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        user.setFirstname(userDetailDto.getFirstname());
        user.setLastname(userDetailDto.getLastname());
        user.setEmailAddress(userDetailDto.getEmailAddress());
        user.setPhoneNumber(userDetailDto.getPhoneNumber());
        user.setCountry(userDetailDto.getCountry());
        user.setPostalCode(userDetailDto.getPostalCode());
        user.setCity(userDetailDto.getCity());
        user.setAddressLine1(userDetailDto.getAddressLine1());
        user.setAddressLine2(userDetailDto.getAddressLine2());
        return userRepository.save(user);
    }

    public List<OrderListDto> getOrderHistory(String publicKey) {
        LOGGER.info("getOrderHistory{}", publicKey);
        userRepository.findUserByPublicKey(publicKey).orElseThrow(() -> new NotFoundException("User not found"));
        List<Order> orders = orderRepository.findOrdersByUser_PublicKey(publicKey).orElse(Collections.emptyList());
        return orderMapper.entityListToDtoList(orders);
    }

    public boolean isValidTransaction(String signature) {
        LOGGER.info("isValidTransaction {}", signature);
        try {
            String jsonRequest = buildJsonRequest(signature);

            RequestBody body = RequestBody.create(jsonRequest, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                .url(SOLANA_RPC_URL)
                .post(body)
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return false;
                }
                String responseBody = response.body().string();
                return parseResponse(responseBody);
            }
        } catch (IOException | JSONException e) {
            return false;
        }
    }

    private String buildJsonRequest(String signature) throws JSONException {
        LOGGER.info("buildJsonRequest{}", signature);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("id", 1);
        jsonObject.put("method", "getSignatureStatuses");
        JSONArray paramsArray = new JSONArray();
        paramsArray.put(new JSONArray().put(signature));
        jsonObject.put("params", paramsArray);
        return jsonObject.toString();
    }

    private boolean parseResponse(String responseBody) throws JSONException {
        LOGGER.info("parseResponse{}", responseBody);
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONObject result = jsonResponse.optJSONObject("result");
        if (result != null) {
            JSONArray value = result.optJSONArray("value");
            if (value != null && value.length() > 0) {
                JSONObject status = value.getJSONObject(0);
                String confirmationStatus = status.optString("confirmationStatus");
                return "confirmed".equals(confirmationStatus) || "finalized".equals(confirmationStatus);
            }
        }
        return false;
    }
}
