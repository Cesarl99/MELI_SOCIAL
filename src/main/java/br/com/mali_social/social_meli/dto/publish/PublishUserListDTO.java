package br.com.mali_social.social_meli.dto.publish;

import java.util.List;

public class PublishUserListDTO {
    private Long user_id;
    private List<PublishDTO> publicacoes;

    public PublishUserListDTO(Long user_id, List<PublishDTO> publicacoes) {
        this.user_id = user_id;
        this.publicacoes = publicacoes;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<PublishDTO> getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(List<PublishDTO> publicacoes) {
        this.publicacoes = publicacoes;
    }

    @Override
    public String toString() {
        return "ListaPublicacaoUsuariosDto{" +
                "user_id=" + user_id +
                ", publicacoes=" + publicacoes +
                '}';
    }
}