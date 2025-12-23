package br.com.mali_social.social_meli.dto;


public class UsuarioDto {
    private long  user_id;
    private String user_name;

    public UsuarioDto(long id, String nome) {
        this.user_id = id;
        this.user_name = nome;
    }

    public long getId() {
        return user_id;
    }

    public void setId(int userId) {
        user_id = userId;
    }

    public String getNome() {
        return user_name;
    }

    public void setNome(String username) {
        user_name = username;
    }

    @Override
    public String toString() {
        return "UsuarioDto{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
