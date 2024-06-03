package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their public key.
     *
     * @param publicKey the public key of the user
     * @return an Optional containing the User if found, or an empty Optional if not
     */
    Optional<User> findUserByPublicKey(String publicKey);

    /**
     * Deletes a user by their public key.
     *
     * @param publicKey the public key of the user
     */
    void deleteByPublicKey(String publicKey);
}

