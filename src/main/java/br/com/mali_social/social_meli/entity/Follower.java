package br.com.mali_social.social_meli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Followers")
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User sellerId;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyerId;

    public Follower() {}

    public Follower(User sellerId, long id, User buyerId) {
        this.sellerId = sellerId;
        this.id = id;
        this.buyerId = buyerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getSellerId() {
        return sellerId;
    }

    public void setSellerId(User sellerId) {
        this.sellerId = sellerId;
    }

    public User getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(User buyerId) {
        this.buyerId = buyerId;
    }
}