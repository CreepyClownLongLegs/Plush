package at.ac.tuwien.sepr.groupphase.backend.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToMany
    private List<PlushToy> plushToys;

    public ProductCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlushToy(PlushToy plushToy) {
        plushToys.add(plushToy);
        plushToy.addProductCategory(this);
    }

    public void removePlushToy(PlushToy plushToy) {
        plushToys.remove(plushToy);
        plushToy.removeProductCategory(this);
    }

    public List<PlushToy> getPlushToys() {
        return plushToys;
    }

}