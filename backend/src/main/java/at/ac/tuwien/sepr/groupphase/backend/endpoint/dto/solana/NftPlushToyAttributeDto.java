package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana;

public class NftPlushToyAttributeDto {
    private String name;
    private String value;

    public NftPlushToyAttributeDto(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NftPlushToyAttributeDto{"
                + "name='" + name + '\''
                + "value='" + value + '\''
                + '}';
    }
}
