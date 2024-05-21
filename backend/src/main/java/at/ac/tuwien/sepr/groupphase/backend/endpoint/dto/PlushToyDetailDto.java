package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;

import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;

public class PlushToyDetailDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private float taxClass;
    private double weight;
    private Size size;
    private Color color;
    private int hp;
    private String imageUrl;
    private float strength;
    private List<ProductCategoryDto> productCategories;

    public PlushToyDetailDto() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public void setProductCategories(List<ProductCategoryDto> productCategories) {
        this.productCategories = productCategories;
    }
}
