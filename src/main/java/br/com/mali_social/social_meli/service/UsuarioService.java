package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.ListaUsuariosSeguindoresDto;
import br.com.mali_social.social_meli.dto.QuatidadeSeguidoresDto;
import br.com.mali_social.social_meli.dto.UsuarioDto;
import br.com.mali_social.social_meli.entity.SeguidoresEntity;
import br.com.mali_social.social_meli.entity.UsuarioEntity;
import br.com.mali_social.social_meli.repository.SeguidoresRepository;
import br.com.mali_social.social_meli.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final SeguidoresRepository seguidoresRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(SeguidoresRepository seguidoresRepository, UsuarioRepository usuarioRepository) {
        this.seguidoresRepository = seguidoresRepository;
        this.usuarioRepository = usuarioRepository;
    }
    public int contarSeguidoresDoVendedor(long vendedorId) {
        return seguidoresRepository.countByVendedorId(vendedorId);
    }

    public String seguir(long UserId, long userIdToFollow){
        SeguidoresEntity seguidores = new SeguidoresEntity();
        seguidores.setVendedor_id(userIdToFollow);
        seguidores.setComprador_id(UserId);
        return seguidoresRepository.save(seguidores).toString();
    }

    public QuatidadeSeguidoresDto contaSeguidores(long UserId){
        int  numeros_seguidores = contarSeguidoresDoVendedor(UserId);
        UsuarioEntity vendedor = usuarioRepository.findById(UserId).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        return mapQtdSeguidoresparaDTO(numeros_seguidores, vendedor);
    }

    public ListaUsuariosSeguindoresDto listaSeguidores(long UserId){
        UsuarioEntity vendedor = usuarioRepository.findById(UserId).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        List<SeguidoresEntity> relacao = seguidoresRepository.findByVendedorId(UserId);
        List<UsuarioDto> seguidores = relacao.stream()
                .map(this::mapRelacaoParaFollowerDTO)
                .toList();
        return mapVendedorEFollowersParaDTO(vendedor, seguidores);
    }


    private QuatidadeSeguidoresDto mapQtdSeguidoresparaDTO(int numeroSeguidores, UsuarioEntity vendedor){
        return new QuatidadeSeguidoresDto(
            vendedor.getId(),
            vendedor.getNome(),
            numeroSeguidores
        );

    }


    private UsuarioDto mapRelacaoParaFollowerDTO(SeguidoresEntity relacao) {

        UsuarioEntity usuario = usuarioRepository.findById(relacao.getComprador_id())
                .orElseThrow(() ->
                        new RuntimeException("Seguidor não encontrado: " + relacao.getComprador_id()));

        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome()
        );
    }

    private ListaUsuariosSeguindoresDto mapVendedorEFollowersParaDTO(UsuarioEntity vendedor, List<UsuarioDto> compradores) {
        return new ListaUsuariosSeguindoresDto(
                vendedor.getId(),
                vendedor.getNome(),
                compradores
        );
    }



}
