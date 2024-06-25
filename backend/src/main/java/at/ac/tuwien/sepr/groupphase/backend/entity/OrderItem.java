package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double pricePerPiece;

    @Column(nullable = false)
    private int position;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = true, length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private double taxAmount;

    @Column(nullable = false)
    private float taxClass;

    @ManyToOne
    private PlushToy plushToy;

    @ManyToOne(optional = true)
    private Order order;

    public OrderItem() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public float getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(float taxClass) {
        this.taxClass = taxClass;
    }

    public PlushToy getPlushToy() {
        return plushToy;
    }

    public void setPlushToy(PlushToy plushToy) {
        this.plushToy = plushToy;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}