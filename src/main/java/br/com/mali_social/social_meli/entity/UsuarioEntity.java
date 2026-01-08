package br.com.mali_social.social_meli.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, length = 100)
    private String nome;

    public UsuarioEntity(Long id, String nome) {
        Id = id;
        this.nome = nome;
    }

    public UsuarioEntity(){}

    public UsuarioEntity(String nome){
        this.nome = nome;
    }

    public long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "Id=" + Id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
