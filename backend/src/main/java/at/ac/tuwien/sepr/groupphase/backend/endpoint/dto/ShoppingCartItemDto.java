package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class ShoppingCartItemDto {
    private String publicKey;
    private long itemId;
    private int amount;

    public ShoppingCartItemDto() {
    }

    public ShoppingCartItemDto(String publicKey, long itemId, int amount) {
        this.publicKey = publicKey;
        this.itemId = itemId;
        this.amount = amount;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
