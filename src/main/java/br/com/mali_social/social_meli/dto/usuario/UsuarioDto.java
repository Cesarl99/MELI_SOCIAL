package br.com.mali_social.social_meli.dto.usuario;


public class UsuarioDto {
    private long  user_id;
    private String user_name;

    public UsuarioDto(long id, String nome) {
        this.user_id = id;
        this.user_name = nome;
    }

    public UsuarioDto() {

    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "UsuarioDto{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
