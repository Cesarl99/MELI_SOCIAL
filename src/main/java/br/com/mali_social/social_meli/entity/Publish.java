package br.com.mali_social.social_meli.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name = "Publish")
public class Publish {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int category;

    @Column(nullable = false)
    private Double price;

    @Column(name = "has_promo", nullable = false)
    private boolean hasPromo;

    @Column(nullable = true)
    private double discount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    public Publish() {
    }

    public Publish(long id, int category, LocalDate date, Double price, boolean has_promo, Product product, double discount, User user) {
        Id = id;
        this.category = category;
        this.date = date;
        this.price = price;
        this.hasPromo = has_promo;
        this.product = product;
        this.discount = discount;
        this.user = user;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isHas_promo() {
        return hasPromo;
    }

    public void setHas_promo(boolean has_promo) {
        this.hasPromo = has_promo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
