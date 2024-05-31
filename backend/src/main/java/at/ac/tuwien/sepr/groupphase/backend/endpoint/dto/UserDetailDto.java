package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

public class UserDetailDto {

    @NotNull(message = "Public Key must not be null")
    @Length(min = 32, max = 44, message = "Public Key must be between 32 and 44 characters long")
    private String publicKey;

    @Size(max = 255, message = "Firstname cannot be longer than 255 characters")
    private String firstname;

    @Size(max = 255, message = "Lastname cannot be longer than 255 characters")
    private String lastname;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email cannot be longer than 255 characters")
    private String emailAddress;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    @Size(max = 255, message = "Phone number cannot be longer than 255 characters")
    private String phoneNumber;

    private boolean locked;

    @Size(max = 255, message = "Country cannot be longer than 255 characters")
    private String country;

    @Size(max = 255, message = "Postal Code cannot be longer than 255 characters")
    private String postalCode;

    @Size(max = 255, message = "City cannot be longer than 255 characters")
    private String city;

    @Size(max = 255, message = "Address Line 1 cannot be longer than 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address Line 2 cannot be longer than 255 characters")
    private String addressLine2;

    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isLocked() {
        return locked;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
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
        if (!(o instanceof UserDetailDto userLoginDto)) {
            return false;
        }
        return Objects.equals(publicKey, userLoginDto.publicKey);
    }

    public static final class UserDtoBuilder {
        private String publicKey;

        private UserDtoBuilder() {
        }

        public static UserDtoBuilder anUserLoginDto() {
            return new UserDtoBuilder();
        }


        public UserDtoBuilder withPublicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public UserDetailDto build() {
            UserDetailDto userLoginDto = new UserDetailDto();
            userLoginDto.setPublicKey(publicKey);
            return userLoginDto;
        }
    }
}
