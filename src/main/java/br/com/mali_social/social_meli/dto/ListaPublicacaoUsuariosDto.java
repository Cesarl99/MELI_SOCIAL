package br.com.mali_social.social_meli.dto;

import java.util.List;

public class ListaPublicacaoUsuariosDto {
    private Long user_id;
    private List<PublicacaoDto> publicacoes;

    public ListaPublicacaoUsuariosDto(Long user_id, List<PublicacaoDto> publicacoes) {
        this.user_id = user_id;
        this.publicacoes = publicacoes;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<PublicacaoDto> getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(List<PublicacaoDto> publicacoes) {
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