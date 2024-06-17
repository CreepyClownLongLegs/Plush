package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class NftPlushToyAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, name = "plush_toy_attribute_value")
    private String value;

    @ManyToOne(optional = false)
    private PlushToyAttribute attribute;

    @ManyToOne(optional = false)
    private Nft nft;

    public NftPlushToyAttributeValue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAttribute(PlushToyAttribute attribute) {
        this.attribute = attribute;
    }

    public PlushToyAttribute getAttribute() {
        return attribute;
    }

    public void setNft(Nft nft) {
        this.nft = nft;
    }

    public Nft getNft() {
        return nft;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}