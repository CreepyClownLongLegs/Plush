package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class NonceDto {
    @NotNull(message = "Nonce must not be null")
    @Length(max = 36, message = "Nonce must not be longer than 36 characters")
    private String nonce;

    public NonceDto(String nonce) {
        this.nonce = nonce;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
