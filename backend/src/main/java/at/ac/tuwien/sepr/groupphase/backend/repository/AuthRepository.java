package at.ac.tuwien.sepr.groupphase.backend.repository;

import at.ac.tuwien.sepr.groupphase.backend.entity.AuthenticationCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthenticationCache, Long> {
    Optional<AuthenticationCache> findAuthCacheByPublicKey(String publicKey);

    void deleteByPublicKey(String publicKey);

    void deleteByTimestampBefore(Instant cutoff);
}

