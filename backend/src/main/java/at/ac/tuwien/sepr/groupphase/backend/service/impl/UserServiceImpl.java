package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import at.ac.tuwien.sepr.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.UserMapper;

import org.springframework.lang.NonNull;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
}
