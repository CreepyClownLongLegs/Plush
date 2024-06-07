package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana;

public class MintNftDto {
    // the public key of the assosiated account as Base58 token
    private String publicKey;

    public MintNftDto(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "MintNftDto{"
                + "publicKey='" + publicKey + '\''
                + '}';
    }
}
