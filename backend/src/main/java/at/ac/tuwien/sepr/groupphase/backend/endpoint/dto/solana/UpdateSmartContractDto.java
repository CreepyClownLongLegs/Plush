package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana;

import java.util.List;

public class UpdateSmartContractDto {
    private String name;
    private String url;
    private String description;
    private String imageUrl;
    private List<NftPlushToyAttributeDto> attributes;

    public UpdateSmartContractDto(String name, String url, String description, String imageUrl,
            List<NftPlushToyAttributeDto> attributes) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.imageUrl = imageUrl;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<NftPlushToyAttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NftPlushToyAttributeDto> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "UpdateSmartContractDto{"
                + "name='" + name + '\''
                + "url='" + url + '\''
                + "description='" + description + '\''
                + "imageUrl='" + imageUrl + '\''
                + "attributes='" + attributes + '\''
                + '}';
    }
}
