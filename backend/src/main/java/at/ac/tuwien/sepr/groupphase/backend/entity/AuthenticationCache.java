package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "authentication")
public class AuthenticationCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 44)
    private String publicKey;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false, length = 36)
    private String nonce;

    public AuthenticationCache(String nonce, String publicKey) {
        this.nonce = nonce;
        this.publicKey = publicKey;
        this.timestamp = Instant.now();
    }

    public AuthenticationCache() {
    }

    public Long getId() {
        return id;
    }

    public String getNonce() {
        return nonce;
    }

}