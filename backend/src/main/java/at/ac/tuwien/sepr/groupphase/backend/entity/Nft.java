package at.ac.tuwien.sepr.groupphase.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Nft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 255)
    private String ownerId;

    @Column(nullable = false, length = 255)
    private String description;

    @ManyToOne
    private PlushToy plushToy;

    @OneToMany(mappedBy = "nft")
    private List<NftPlushToyAttributeValue> attributes;

    public Nft(String name, LocalDateTime timestamp, String ownerId, String description) {
        this.name = name;
        this.timestamp = timestamp;
        this.ownerId = ownerId;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlushToy getPlushToy() {
        return plushToy;
    }

    public void setPlushToy(PlushToy plushToy) {
        this.plushToy = plushToy;
    }

    public List<NftPlushToyAttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<NftPlushToyAttributeValue> attributes) {
        this.attributes = attributes;
    }
}