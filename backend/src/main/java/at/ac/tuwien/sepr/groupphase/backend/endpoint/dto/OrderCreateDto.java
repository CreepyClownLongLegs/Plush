package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class OrderCreateDto {
    @NotNull(message = "Signature must not be null")
    @Length(max = 128, message = "Signature must not be longer than 128 characters")
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
