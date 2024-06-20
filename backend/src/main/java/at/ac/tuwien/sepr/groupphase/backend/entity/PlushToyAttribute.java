package at.ac.tuwien.sepr.groupphase.backend.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class PlushToyAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @OneToMany(mappedBy = "attribute", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PlushToyAttributeDistribution> distributions;

    public PlushToyAttribute(String name) {
        this.name = name;
    }

    public PlushToyAttribute() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlushToyAttributeDistribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<PlushToyAttributeDistribution> distributions) {
        this.distributions = distributions;
    }
}
