package br.com.mali_social.social_meli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seguidores")
public class SeguidoresEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private UsuarioEntity vendedorId;

    @ManyToOne
    @JoinColumn(name = "comprador_id", nullable = false)
    private UsuarioEntity compradorId;

    public SeguidoresEntity() {}

    public SeguidoresEntity(UsuarioEntity vendedorId, long id, UsuarioEntity compradorId) {
        this.vendedorId = vendedorId;
        this.id = id;
        this.compradorId = compradorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UsuarioEntity getCompradorId() {
        return compradorId;
    }

    public void setCompradorId(UsuarioEntity compradorId) {
        this.compradorId = compradorId;
    }

    public UsuarioEntity getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(UsuarioEntity vendedorId) {
        this.vendedorId = vendedorId;
    }
}