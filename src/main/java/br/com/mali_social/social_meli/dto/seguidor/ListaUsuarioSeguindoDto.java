package br.com.mali_social.social_meli.dto.seguidor;

import br.com.mali_social.social_meli.dto.usuario.UsuarioDto;

import java.util.List;

public class ListaUsuarioSeguindoDto {
    private long usuarioId;
    private String nomeUsuario;
    private List<UsuarioDto> seguindo;

    public ListaUsuarioSeguindoDto(long usuarioId, String nomeUsuario, List<UsuarioDto> seguindo) {
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

    public List<UsuarioDto> getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(List<UsuarioDto> seguindo) {
        this.seguindo = seguindo;
    }

}
