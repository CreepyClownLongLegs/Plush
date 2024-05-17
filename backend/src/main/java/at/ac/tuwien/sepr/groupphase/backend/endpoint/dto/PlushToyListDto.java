package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class PlushToyListDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Double averageRating;
    private int hp;
    private String imageUrl;

    public PlushToyListDto() {
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
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

}
