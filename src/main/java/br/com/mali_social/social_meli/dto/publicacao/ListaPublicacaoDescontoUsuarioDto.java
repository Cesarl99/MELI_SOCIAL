package br.com.mali_social.social_meli.dto.publicacao;

import java.util.List;

public class ListaPublicacaoDescontoUsuarioDto {
    private Long user_id;
    private String user_name;
    private List<PublicacaoDto> posts;


    public ListaPublicacaoDescontoUsuarioDto(Long user_id, String user_name, List<PublicacaoDto> posts) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.posts = posts;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<PublicacaoDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PublicacaoDto> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "ListaPublicacaoDescontoUsuarioDto{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", posts=" + posts +
                '}';
    }
}
