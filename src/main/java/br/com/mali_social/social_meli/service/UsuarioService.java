package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.ListaUsuariosSeguindoresDto;
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
    public long contarSeguidoresDoVendedor(long vendedorId) {
        return seguidoresRepository.countByVendedorId(vendedorId);
    }

    public String seguir(long UserId, long userIdToFollow){
        System.out.println("USUARIO PARA SERGUIR ");
        System.out.println("CHAMANDO O REPOSITRY");
        SeguidoresEntity seguidores = new SeguidoresEntity();
        seguidores.setVendedor_id(userIdToFollow);
        seguidores.setComprador_id(UserId);
        return seguidoresRepository.save(seguidores).toString();
    }

    public long contaSeguidores(long UserId){
        long numeros_seguidores = contarSeguidoresDoVendedor(UserId);
        System.out.println("USUARIO COM O ID " + UserId + "possue " + numeros_seguidores);
        return numeros_seguidores;
    }

    public ListaUsuariosSeguindoresDto listaSeguidores(long UserId){
        UsuarioEntity vendedor = usuarioRepository.findById(UserId).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        List<SeguidoresEntity> relacao = seguidoresRepository.findByVendedorId(UserId);
        System.out.println("valor de relacao" + relacao.toString());
        List<UsuarioDto> seguidores = relacao.stream()
                .map(this::mapRelacaoParaFollowerDTO)
                .toList();
        System.out.println("valor do seguidores" + seguidores.toString());
        return mapVendedorEFollowersParaDTO(vendedor, seguidores);
    }


    private UsuarioDto mapRelacaoParaFollowerDTO(SeguidoresEntity relacao) {
        System.out.println("dentro do maprelacao" + relacao.toString());
        UsuarioEntity usuario = usuarioRepository.findById(relacao.getComprador_id())
                .orElseThrow(() ->
                        new RuntimeException("Seguidor não encontrado: " + relacao.getComprador_id()));

        System.out.println("valor do usuarios" + usuario.getId() + usuario.getNome());

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
