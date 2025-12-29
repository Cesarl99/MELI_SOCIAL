package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.ListaPublicacaoUsuariosDto;
import br.com.mali_social.social_meli.dto.ProdutoDto;
import br.com.mali_social.social_meli.dto.PublicacaoDto;
import br.com.mali_social.social_meli.dto.UsuarioDto;
import br.com.mali_social.social_meli.entity.ProdutosEntity;
import br.com.mali_social.social_meli.entity.PublicacaoEntity;
import br.com.mali_social.social_meli.entity.SeguidoresEntity;
import br.com.mali_social.social_meli.entity.UsuarioEntity;
import br.com.mali_social.social_meli.repository.PublicacaoRepository;
import br.com.mali_social.social_meli.repository.SeguidoresRepository;
import br.com.mali_social.social_meli.repository.UsuarioRepository;
import br.com.mali_social.social_meli.util.Verificacao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class PublicacaoService {
    private final PublicacaoRepository publicacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeguidoresRepository seguidoresRepository;
    private final ProdutoService produtoService;

    public PublicacaoService (PublicacaoRepository publicacaoRepository, UsuarioRepository usuarioRepository, SeguidoresRepository seguidoresRepository, ProdutoService produtoService){
        this.publicacaoRepository = publicacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.seguidoresRepository = seguidoresRepository;
        this.produtoService = produtoService;
    }

    public String salvarPublicacao(PublicacaoDto publicacaoDto, ProdutosEntity produtosEntity){
        PublicacaoEntity publicacao = new PublicacaoEntity();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        publicacao.setData(LocalDate.parse(publicacaoDto.getDate(),formato));
        publicacao.setCategoria(publicacaoDto.getCategory());
        publicacao.setPreco(publicacaoDto.getPrice());
        publicacao.setPreco(publicacaoDto.getPrice());

        UsuarioEntity usuarioProduto = usuarioRepository.findById(publicacaoDto.getUser_id()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));;

        publicacao.setUsuario(usuarioProduto);

        publicacao.setProduto(produtosEntity);

        return (publicacaoRepository.save(publicacao).toString());
    }

    public ListaPublicacaoUsuariosDto ListaPublicacaoUsuario(Long UserId) {
        List<SeguidoresEntity> relacao = seguidoresRepository.findByCompradorId(UserId);
        List<PublicacaoDto> publicacoes = new ArrayList<>();
        for (int i = 0; i < relacao.size(); i ++ ){
            List<PublicacaoDto> PublicacaoAux = mapRelacaoPublicaoUsuarioDTO(relacao.get(i));
            for (int x = 0; x < PublicacaoAux.size(); x ++) {
                publicacoes.add(PublicacaoAux.get(x));
            }
        }
        return mapPublicacaoUsuario(UserId, publicacoes);
    }

    private List<PublicacaoDto> mapRelacaoPublicaoUsuarioDTO (SeguidoresEntity relacao){
        List<PublicacaoEntity> publicacoes = publicacaoRepository.findByUsuarioId(relacao.getVendedor_id());
        if(publicacoes.isEmpty()){
            return List.of();
        }

        DateTimeFormatter padraoHora = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return publicacoes.stream()
                .map(pub->{
                    ProdutoDto produtoDto = produtoService.mapProdutoEntityToDto(pub.getProduto());
                    return new PublicacaoDto(
                            pub.getId(),
                            pub.getUsuario().getId(),
                            pub.getData().format(padraoHora),
                            produtoDto,
                            pub.getCategoria(),
                            pub.getPreco(),
                            pub.isPromocao(),
                            pub.getDesconto()
                    );
                })
                .toList();
        }

    private ListaPublicacaoUsuariosDto mapPublicacaoUsuario(long UserId, List<PublicacaoDto> publicacoes ){
        return new ListaPublicacaoUsuariosDto(
            UserId,
            publicacoes
        );
    }

}
