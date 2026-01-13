package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.seguidor.ListaUsuarioSeguindoDto;
import br.com.mali_social.social_meli.dto.seguidor.ListaUsuariosSeguindoresDto;
import br.com.mali_social.social_meli.dto.seguidor.QuatidadeSeguidoresDto;
import br.com.mali_social.social_meli.dto.usuario.UsuarioDto;
import br.com.mali_social.social_meli.entity.SeguidoresEntity;
import br.com.mali_social.social_meli.entity.UsuarioEntity;
import br.com.mali_social.social_meli.repository.SeguidoresRepository;
import br.com.mali_social.social_meli.repository.UsuarioRepository;
import br.com.mali_social.social_meli.util.Verificacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class UsuarioService {
    private final SeguidoresRepository seguidoresRepository;
    private final UsuarioRepository usuarioRepository;
    private final Verificacao verificacao = new Verificacao();

    public UsuarioService(SeguidoresRepository seguidoresRepository, UsuarioRepository usuarioRepository) {
        this.seguidoresRepository = seguidoresRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void salvarUsuario(UsuarioDto usuario) {
        UsuarioEntity usuarioEntity = new UsuarioEntity(usuario.getUser_id(), usuario.getUser_name());
        usuarioRepository.save(usuarioEntity);
    }

    public UsuarioDto pesquisaUsuarios(long UserId){
        UsuarioEntity usuarioEnt = usuarioRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (comprador) não encontrado."));


        return new UsuarioDto(
                usuarioEnt.getId(),
                usuarioEnt.getNome()
        );
    }

    public void seguir(long UserId, long userIdToFollow){
        SeguidoresEntity seguidores = new SeguidoresEntity();
        verificacao.verificaIdUsuario(UserId);
        verificacao.verificaIdUsuario(userIdToFollow);
        UsuarioEntity vendedor = usuarioRepository.findById(userIdToFollow).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (vendedor) não encontrado."));
        UsuarioEntity comprador = usuarioRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (comprador) não encontrado."));
        seguidores.setVendedorId(vendedor);
        seguidores.setCompradorId(comprador);
        seguidoresRepository.save(seguidores).toString();
    }

    public QuatidadeSeguidoresDto contaSeguidores(long UserId){
        verificacao.verificaIdUsuario(UserId);
        UsuarioEntity vendedor = usuarioRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario não encontrado."));
        int numeros_seguidores = seguidoresRepository.countByVendedorId(vendedor);
        return mapQtdSeguidoresparaDTO(numeros_seguidores, vendedor);
    }

    public ListaUsuariosSeguindoresDto listaSeguidores(long UserId, String order){
        verificacao.verificaIdUsuario(UserId);
        UsuarioEntity vendedor = usuarioRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario não encontrado."));
        List<SeguidoresEntity> relacao = seguidoresRepository.findByVendedorId(vendedor);
        List<UsuarioDto> seguidores = new java.util.ArrayList<>(relacao.stream()
                .map(this::mapRelacaoVendorCompradorDTO)
                .toList());

        switch (order){
            case "name_asc":
                seguidores.sort(Comparator.comparing(UsuarioDto::getUser_name));
                break;
            case "name_desc":
                seguidores.sort(Comparator.comparing(UsuarioDto::getUser_name).reversed());
                break;
        }
        return mapVendedorECompradorParaDTO(vendedor, seguidores);
    }

    public ListaUsuarioSeguindoDto listaSeguindo(long UserId, String order){
        verificacao.verificaIdUsuario(UserId);
        UsuarioEntity comprador = usuarioRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario não encontrado."));
        List<SeguidoresEntity> relacao = seguidoresRepository.findByCompradorId(comprador);
        List<UsuarioDto> seguidores = new java.util.ArrayList<>(relacao.stream()
                .map(this::mapRelacaoCompradorVendedorDTO)
                .toList());

        switch (order){
            case "name_asc":
                seguidores.sort(Comparator.comparing(UsuarioDto::getUser_name));
                break;
            case "name_desc":
                seguidores.sort(Comparator.comparing(UsuarioDto::getUser_name).reversed());
                break;
        }
        return mapCompradoresEVendedorParaDTO(comprador, seguidores);
    }

    public void deixarDeSeguir(long compradorId, long vendedorId){
        verificacao.verificaIdUsuario(compradorId);
        UsuarioEntity comprador = usuarioRepository.findById(compradorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (comprador) não encontrado."));
        UsuarioEntity vendedor = usuarioRepository.findById(vendedorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (vendedor) não encontrado."));

        seguidoresRepository.deleteByCompradorIdAndVendedorId(comprador, vendedor);
    }


    private QuatidadeSeguidoresDto mapQtdSeguidoresparaDTO(int numeroSeguidores, UsuarioEntity vendedor){
        return new QuatidadeSeguidoresDto(
            vendedor.getId(),
            vendedor.getNome(),
            numeroSeguidores
        );

    }

    private UsuarioDto mapRelacaoVendorCompradorDTO(SeguidoresEntity relacao) {

        UsuarioEntity usuario = usuarioRepository.findById(relacao.getCompradorId().getId())
                .orElseThrow(() ->
                        new RuntimeException("Seguidor não encontrado: " + relacao.getCompradorId().getId()));

        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome()
        );

    }

    private UsuarioDto mapRelacaoCompradorVendedorDTO(SeguidoresEntity relacao) {

        UsuarioEntity usuario = usuarioRepository.findById(relacao.getVendedorId().getId())
                .orElseThrow(() ->
                        new RuntimeException("Seguidor não encontrado: " + relacao.getVendedorId().getId()));

        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome()
        );
    }

    private ListaUsuariosSeguindoresDto mapVendedorECompradorParaDTO(UsuarioEntity vendedor, List<UsuarioDto> compradores) {
        return new ListaUsuariosSeguindoresDto(
                vendedor.getId(),
                vendedor.getNome(),
                compradores
        );
    }

    private ListaUsuarioSeguindoDto mapCompradoresEVendedorParaDTO(UsuarioEntity comprador, List<UsuarioDto> vendedores) {
        return new ListaUsuarioSeguindoDto(
                comprador.getId(),
                comprador.getNome(),
                vendedores
        );
    }
}
