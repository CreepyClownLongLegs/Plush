package at.ac.tuwien.sepr.groupphase.backend.entity;

import java.sql.Blob;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class PlushToy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = true)
    private double averageRating;

    @Column(nullable = true, length = 1024)
    private String description;

    @Column(nullable = false)
    private float taxClass;

    @Column(nullable = true)
    private double taxAmount;

    @Column(nullable = false)
    private double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    @Lob
    @Column(nullable = true)
    private Blob image;

    @ManyToMany(mappedBy = "plushToys")
    private List<ProductCategory> productCategories;

    @OneToMany(mappedBy = "plushToy")
    private List<Nft> nfts;

    @OneToMany(mappedBy = "plushToy", fetch = FetchType.EAGER)
    private List<PlushToyAttributeDistribution> attributeDistributions;

    @OneToOne
    private SmartContract smartContract;

    public PlushToy() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(float taxClass) {
        this.taxClass = taxClass;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void addProductCategory(ProductCategory productCategory) {
        productCategories.add(productCategory);
        productCategory.addPlushToy(this);
    }

    public void removeProductCategory(ProductCategory productCategory) {
        productCategories.remove(productCategory);
        productCategory.removePlushToy(this);
    }

    public void addNft(Nft nft) {
        nfts.add(nft);
        nft.setPlushToy(this);
    }

    public void setSmartContract(SmartContract smartContract) {
        this.smartContract = smartContract;
    }

    public SmartContract getSmartContract() {
        return smartContract;
    }

    public List<PlushToyAttribute> getPlushToyAttributes() {
        return attributeDistributions.stream()
                .map(PlushToyAttributeDistribution::getAttribute)
                .collect(Collectors.toList());
    }

    public List<PlushToyAttributeDistribution> getAttributeDistributions() {
        return attributeDistributions;
    }

    public void addAttributeDistribution(PlushToyAttributeDistribution attributeDistribution) {
        attributeDistributions.add(attributeDistribution);
        attributeDistribution.setPlushToy(this);
    }

}