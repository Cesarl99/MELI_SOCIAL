package br.com.mali_social.social_meli.dto.seguidor;

import br.com.mali_social.social_meli.dto.usuario.UsuarioDto;

import java.util.List;

public class ListaUsuariosSeguindoresDto {
    private long usuarioId;
    private String nomeUsuario;
    private List<UsuarioDto> seguidores;

    public ListaUsuariosSeguindoresDto(long usuarioId, String nomeUsuario, List<UsuarioDto> seguidores) {
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

    public List<UsuarioDto> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<UsuarioDto> seguidores) {
        this.seguidores = seguidores;
    }
}
