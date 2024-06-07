package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana;

public class PublicKeyDto {
    // the public key of the smart contract as Base58 token
    private String publicKey;

    public PublicKeyDto(String publicKey) {
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
        return "PublicKeyDto{"
                + "publicKey='" + publicKey + '\''
                + '}';
    }
}
