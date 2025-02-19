package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.solana;

public class CreateSmartContractDto {
    private String name;
    private String url;

    public CreateSmartContractDto(String name, String url) {
        this.name = name;
        this.url = url;
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

    @Override
    public String toString() {
        return "CreateSmartContractDto{"
                + "name='" + name + '\''
                + "url='" + url + '\''
                + '}';
    }
}
