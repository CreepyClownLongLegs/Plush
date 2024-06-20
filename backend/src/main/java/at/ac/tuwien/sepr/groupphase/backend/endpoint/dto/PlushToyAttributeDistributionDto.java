package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotNull;

public class PlushToyAttributeDistributionDto {
    private Long id;
    @NotNull(message = "Name must not be null")
    @Length(max = 255, message = "Name must not be longer than 255 characters")
    private String name;
    @NotNull(message = "Chance must not be null")
    @Range(min = 0, message = "Chance must not be negative")
    @Range(max = 100, message = "Chance must not be greater than 100")
    private float quantityPercentage;
    @NotNull(message = "Attribute must not be null")
    private PlushToyAttributeDto attribute;

    public PlushToyAttributeDistributionDto() {
    }

    public PlushToyAttributeDistributionDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantityPercentage() {
        return quantityPercentage;
    }

    public void setQuantityPercentage(float quantityPercentage) {
        this.quantityPercentage = quantityPercentage;
    }

    public PlushToyAttributeDto getAttribute() {
        return attribute;
    }

    public void setAttribute(PlushToyAttributeDto attribute) {
        this.attribute = attribute;
    }
}
