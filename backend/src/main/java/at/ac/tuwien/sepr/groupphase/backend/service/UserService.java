package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.OrderListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UserService {

    /**
     * Deletes a user from the system using their public key.
     *
     * @param publicKey the public key of the user to be deleted
     * @throws NotFoundException if a user with the given public key does not exist
     */
    void deleteUser(@NonNull String publicKey) throws NotFoundException;

    /**
     * Retrieves the order history for a user identified by their public key.
     *
     * @param publicKey the public key of the user whose order history is to be retrieved
     * @return a list of Order objects representing the user's order history
     * @throws NotFoundException if a user with the given public key does not exist
     */
    List<OrderListDto> getOrderHistory(String publicKey) throws NotFoundException;

    /**
     * Finds a User By their public key.
     *
     * @param publicKey is the public key of the user to be found
     * @return an object of type User
     * @throws java.lang.ClassNotFoundException if a user cannot be found
     */
    User findUserByPublicKey(@NonNull String publicKey) throws NotFoundException;

    /**
     * Updates a user by their public key.
     *
     * @param publicKey is the key of the User to be updates
     * @return an object of type User
     * @throws java.lang.ClassNotFoundException if a user cannot be found
     */
    User updateUser(String publicKey, UserDetailDto userDetailDto) throws NotFoundException;
}
