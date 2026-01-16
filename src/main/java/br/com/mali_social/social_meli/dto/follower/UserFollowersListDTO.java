package br.com.mali_social.social_meli.dto.follower;

import br.com.mali_social.social_meli.dto.user.UserDTO;

import java.util.List;

public class UserFollowersListDTO {
    private long usuarioId;
    private String nomeUsuario;
    private List<UserDTO> seguidores;

    public UserFollowersListDTO(long usuarioId, String nomeUsuario, List<UserDTO> seguidores) {
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.seguidores = seguidores;
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

    public List<UserDTO> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<UserDTO> seguidores) {
        this.seguidores = seguidores;
    }
}
