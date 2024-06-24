package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class SearchPlushToyDto {
    private String name;
    private Long categoryId = -1L;

    public SearchPlushToyDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}