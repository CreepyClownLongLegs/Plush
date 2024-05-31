package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserDetailDto;

import org.springframework.lang.NonNull;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;

public interface UserService {

    /**
     * Deletes a user from the system using their public key.
     *
     * @param publicKey is the public key of the user to be deleted
     * @throws NotFoundException if a user with the given public key does not exist
     */
    void deleteUser(@NonNull String publicKey) throws NotFoundException;

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
