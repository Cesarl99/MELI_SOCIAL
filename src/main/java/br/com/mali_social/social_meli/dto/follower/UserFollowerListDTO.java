package br.com.mali_social.social_meli.dto.follower;

import br.com.mali_social.social_meli.dto.user.UserDTO;

import java.util.List;

public class UserFollowerListDTO {
    private long usuarioId;
    private String nomeUsuario;
    private List<UserDTO> seguindo;

    public UserFollowerListDTO(long usuarioId, String nomeUsuario, List<UserDTO> seguindo) {
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.seguindo = seguindo;
    }
    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public List<UserDTO> getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(List<UserDTO> seguindo) {
        this.seguindo = seguindo;
    }

}
