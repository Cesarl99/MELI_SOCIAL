package br.com.mali_social.social_meli.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table (name = "publicacao")
public class PublicacaoEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private int categoria;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private boolean promocao;

    @Column(nullable = true)
    private double desconto;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutosEntity produto;


    public PublicacaoEntity(){}

    public PublicacaoEntity(long id, ProdutosEntity produto, UsuarioEntity usuario, boolean promocao, double desconto, Double preco, int categoria, LocalDate data) {
        Id = id;
        this.produto = produto;
        this.usuario = usuario;
        this.promocao = promocao;
        this.desconto = desconto;
        this.preco = preco;
        this.categoria = categoria;
        this.data = data;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public ProdutosEntity getProduto() {
        return produto;
    }

    public void setProduto(ProdutosEntity produto) {
        this.produto = produto;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public boolean isPromocao() {
        return promocao;
    }

    public void setPromocao(boolean promocao) {
        this.promocao = promocao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }



    @Override
    public String toString() {
        return "PublicacaoEntity{" +
                "Id=" + Id +
                ", data=" + data +
                ", categoria=" + categoria +
                ", preco=" + preco +
                ", promocao=" + promocao +
                ", desconto=" + desconto +
                ", usuario=" + usuario +
                ", produto=" + produto +
                '}';
    }
}
