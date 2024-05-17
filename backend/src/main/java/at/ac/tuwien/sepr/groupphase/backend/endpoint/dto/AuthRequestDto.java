package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public class AuthRequestDto {

    @NotNull(message = "Public Key must not be null")
    @Length(max = 44, message = "Public Key must not be longer than 44 characters")
    private String publicKey;

    @NotNull(message = "Signature must not be null")
    @Length(max = 64, message = "Signature must not be longer than 64 characters")
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthRequestDto userLoginDto)) {
            return false;
        }
        return Objects.equals(publicKey, userLoginDto.publicKey);
    }

    @Override
    public String toString() {
        return "UserLoginDto{"
            + "publicKey='" + publicKey
            + '}';
    }
}
