package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

public class ProductCategoryCreationDto {
    @NotNull(message = "Name must not be null")
    @Length(max = 255, message = "Name must not be longer than 255 characters")
    private String name;

    public ProductCategoryCreationDto() {
    }

    public ProductCategoryCreationDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
