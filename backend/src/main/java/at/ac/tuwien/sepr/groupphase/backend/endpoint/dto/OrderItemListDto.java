package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class OrderItemListDto {
    private Long id;
    private double pricePerPiece;
    private String name;
    private int amount;
    private String imageUrl;
    private Long plushToyId;

    public OrderItemListDto() {
    }

    public Long getPlushToyId() {
        return plushToyId;
    }

    public void setPlushToyId(Long plushToyId) {
        this.plushToyId = plushToyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
