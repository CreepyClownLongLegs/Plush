package at.ac.tuwien.sepr.groupphase.backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "application_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 44)
    private String publicKey;

    @Column(nullable = true, length = 255)
    private String firstname;

    @Column(nullable = true, length = 255)
    private String lastname;

    @Column(nullable = true, length = 255)
    private String emailAddress;

    @Column(nullable = true, length = 255)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean locked;

    @Column(nullable = true, length = 255)
    private String country;

    @Column(nullable = true, length = 255)
    private String postalCode;

    @Column(nullable = true, length = 255)
    private String city;

    @Column(nullable = true, length = 255)
    private String addressLine1;

    @Column(nullable = true, length = 255)
    private String addressLine2;

    @Column(nullable = false)
    private boolean isAdmin = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ShoppingCartItem> shoppingCartItems;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User() {
    }

    public User(String publicKey) {
        this.publicKey = publicKey;
    }

    public User(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void addShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        shoppingCartItems.add(shoppingCartItem);
        shoppingCartItem.setUser(this);
    }

    public void removeShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        shoppingCartItems.remove(shoppingCartItem);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
}