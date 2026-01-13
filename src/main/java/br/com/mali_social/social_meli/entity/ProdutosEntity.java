package br.com.mali_social.social_meli.entity;


import br.com.mali_social.social_meli.service.PublicacaoService;
import jakarta.persistence.*;

@Entity
@Table(name = "produto")
public class ProdutosEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable = false, length = 40)
    private String nome_produto;

    @Column(nullable = false, length = 15)
    private String tipo;

    @Column(nullable = false, length = 25)
    private String marca;

    @Column(nullable = false, length = 15)
    private String cor;

    @Column(nullable = false, length = 80)
    private String notas;

    public ProdutosEntity(){}

    public ProdutosEntity (String nomeProduto, String tipo, String marca, String cor, String notas){
        this.nome_produto = nomeProduto;
        this.tipo = tipo;
        this.marca = marca;
        this.cor = cor;
        this.notas = notas;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return "ProdutosEntity{" +
                "Id=" + Id +
                ", nome_produto='" + nome_produto + '\'' +
                ", tipo='" + tipo + '\'' +
                ", marca='" + marca + '\'' +
                ", cor='" + cor + '\'' +
                ", notas='" + notas + '\'' +
                '}';
    }
}
