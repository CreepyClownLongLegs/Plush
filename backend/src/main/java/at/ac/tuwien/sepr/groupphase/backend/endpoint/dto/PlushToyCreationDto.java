package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a plush toy.
 */
public class PlushToyCreationDto {
    @NotNull(message = "Name must not be null")
    @Length(max = 255, message = "Name must not be longer than 255 characters")
    public String name;
    @NotNull(message = "Price must not be null")
    @Range(min = 0, message = "Price must not be negative")
    public double price;
    @Length(max = 1024, message = "Description must not be longer than 1024 characters")
    public String description;
    @NotNull(message = "Tax class must not be null")
    public float taxClass;
    @NotNull(message = "Weight must not be null")
    public double weight;
    @NotNull(message = "Color must not be null")
    public Color color;
    @NotNull(message = "Size must not be null")
    public Size size;
    
    @Override
    public String toString() {
        return "PlushToyCreationDto [name=" + name + ", price=" + price + ", description=" + description + ", taxClass="
                + taxClass + ", weight=" + weight + ", color=" + color + ", size=" + size + "]";
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(float taxClass) {
        this.taxClass = taxClass;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Size getSize() {
        return size;
    }
    
    public void setSize(Size size) {
        this.size = size;
    }
}
