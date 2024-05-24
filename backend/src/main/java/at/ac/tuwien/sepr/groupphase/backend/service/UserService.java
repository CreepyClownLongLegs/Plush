package at.ac.tuwien.sepr.groupphase.backend.service;

import org.springframework.lang.NonNull;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

public interface UserService {

    /**
     * Deletes a user from the system using their public key.
     *
     * @param publicKey the public key of the user to be deleted
     * @throws NotFoundException if a user with the given public key does not exist
     */
    void deleteUser(@NonNull String publicKey) throws NotFoundException;
}
