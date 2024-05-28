package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;

import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class PlushToyDetailDto {
    private Long id;
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
    @NotNull(message = "HP must not be null")
    public int hp;
    @Length(max = 255, message = "ImageUrl must not be longer than 255 characters")
    public String imageUrl;
    @Range(min = 0, message = "Strength must not be negative")
    public float strength;
    private List<ProductCategoryDto> productCategories;

    public PlushToyDetailDto() {
    }

    @Override
    public String toString() {
        return "PlushToyCreationDto [name=" + name + ", price=" + price + ", description=" + description + ", taxClass="
            + taxClass + ", weight=" + weight + ", color=" + color + ", size=" + size + ", hp=" + hp + ", imageUrl="
            + imageUrl + ", strength=" + strength + "]";
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public List<ProductCategoryDto> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategoryDto> categories) {
        this.productCategories = categories;
    }
}
