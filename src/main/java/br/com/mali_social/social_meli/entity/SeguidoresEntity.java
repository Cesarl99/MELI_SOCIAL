package br.com.mali_social.social_meli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seguidores")
public class SeguidoresEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long compradorId;

    @Column(nullable = false)
    private long vendedorId;

    public SeguidoresEntity() {}

    public SeguidoresEntity(long idComprador, long idVendedor) {
        this.compradorId = idComprador;
        this.vendedorId = idVendedor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getComprador_id() {
        return compradorId;
    }

    public void setComprador_id(long compradorId) { // nome igual ao campo
        this.compradorId = compradorId;
    }

    public long getVendedor_id() {
        return vendedorId;
    }

    public void setVendedor_id(long vendedorId) {
        this.vendedorId = vendedorId;
    }

    @Override
    public String toString() {
        return "SeguidoresEntity{" +
                "id=" + id +
                ", compradorId=" + compradorId +
                ", vendedorId=" + vendedorId +
                '}';
    }
}