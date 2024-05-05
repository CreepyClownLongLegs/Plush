package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PlushToyAttributeDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private float quantityPercentage;

    @ManyToOne(optional = false)
    private PlushToyAttribute attribute;

    @ManyToOne(optional = false)
    private PlushToy plushToy;

    public PlushToyAttributeDistribution() {
    }

    public Long getId() {
        return id;
    }

    public void setPlushToy(PlushToy plushToy) {
        this.plushToy = plushToy;
    }

    public PlushToy getPlushToy() {
        return plushToy;
    }

    public void setAttribute(PlushToyAttribute attribute) {
        this.attribute = attribute;
    }

    public PlushToyAttribute getAttribute() {
        return attribute;
    }

    public void setQuantityPercentage(float quantityPercentage) {
        this.quantityPercentage = quantityPercentage;
    }

    public float getQuantityPercentage() {
        return quantityPercentage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}