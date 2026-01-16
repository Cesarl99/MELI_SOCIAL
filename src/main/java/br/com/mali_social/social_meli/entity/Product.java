package br.com.mali_social.social_meli.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable = false, length = 40)
    private String product_name;

    @Column(nullable = false, length = 15)
    private String type;

    @Column(nullable = false, length = 25)
    private String brand;

    @Column(nullable = false, length = 15)
    private String color;

    @Column(nullable = false, length = 80)
    private String notes;


    public Product() {
    }

    public Product(long id, String color, String notes, String brand, String type, String product_name) {
        Id = id;
        this.color = color;
        this.notes = notes;
        this.brand = brand;
        this.type = type;
        this.product_name = product_name;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
