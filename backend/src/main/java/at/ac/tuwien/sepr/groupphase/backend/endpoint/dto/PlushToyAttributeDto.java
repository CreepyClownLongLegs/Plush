package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

public class PlushToyAttributeDto {
    private Long id;

    @NotNull(message = "Name must not be null")
    @Length(max = 255, message = "Name must not be longer than 255 characters")
    private String name;

    public PlushToyAttributeDto() {
    }

    public PlushToyAttributeDto(Long id, String name) {
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

}
