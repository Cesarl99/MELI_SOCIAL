package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.produto.ProdutoDto;
import br.com.mali_social.social_meli.dto.publicacao.ListaPublicacaoDescontoUsuarioDto;
import br.com.mali_social.social_meli.dto.publicacao.ListaPublicacaoUsuariosDto;
import br.com.mali_social.social_meli.dto.publicacao.PublicacaoDto;
import br.com.mali_social.social_meli.dto.publicacao.QuantidadePublicacaoDescontoDto;
import br.com.mali_social.social_meli.entity.ProdutosEntity;
import br.com.mali_social.social_meli.entity.PublicacaoEntity;
import br.com.mali_social.social_meli.entity.SeguidoresEntity;
import br.com.mali_social.social_meli.entity.UsuarioEntity;
import br.com.mali_social.social_meli.repository.PublicacaoRepository;
import br.com.mali_social.social_meli.repository.SeguidoresRepository;
import br.com.mali_social.social_meli.repository.UsuarioRepository;
import br.com.mali_social.social_meli.util.Verificacao;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final Verificacao verificacao = new Verificacao();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PublicacaoService (PublicacaoRepository publicacaoRepository, UsuarioRepository usuarioRepository, SeguidoresRepository seguidoresRepository, ProdutoService produtoService){
        this.publicacaoRepository = publicacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.seguidoresRepository = seguidoresRepository;
        this.produtoService = produtoService;
    }

    public String salvarPublicacao(PublicacaoDto publicacaoDto, ProdutosEntity produtosEntity){
        PublicacaoEntity publicacao = new PublicacaoEntity();
        publicacaoDto.getProduct().setProduct_id(produtosEntity.getId());
        verificacao.verificaCadastroProduto(publicacaoDto);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        publicacao.setData(LocalDate.parse(publicacaoDto.getDate(),formato));
        publicacao.setCategoria(publicacaoDto.getCategory());
        publicacao.setPreco(publicacaoDto.getPrice());
        publicacao.setPromocao(publicacaoDto.isHas_promo());
        publicacao.setDesconto(publicacaoDto.getDiscount());


        UsuarioEntity usuarioProduto = usuarioRepository.findById(publicacaoDto.getUser_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario não encontrado."));

        publicacao.setUsuario(usuarioProduto);

        publicacao.setProduto(produtosEntity);

        return (publicacaoRepository.save(publicacao).toString());
    }

    public ListaPublicacaoUsuariosDto listaPublicacaoUsuario(Long user_id, String order) {

        verificacao.verificaIdUsuario(user_id);
        UsuarioEntity comprador = usuarioRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        List<SeguidoresEntity> relacao = seguidoresRepository.findByCompradorId(comprador);

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        List<Long> idsVendedores = relacao.stream()
                .map(SeguidoresEntity::getVendedorId)
                .map(UsuarioEntity::getId)
                .distinct()
                .toList();

        List<PublicacaoEntity> publicacoesEnt = new ArrayList<>();
        List<PublicacaoDto> publicacoes = new ArrayList<>();

        if ("date_desc".equals(order)){
            publicacoesEnt = publicacaoRepository.findByUsuarioIdInAndDataBetweenOrderByDataDesc(idsVendedores,dataLimite, hoje);
        } else {
            publicacoesEnt = publicacaoRepository.findByUsuarioIdInAndDataBetweenOrderByDataAsc(idsVendedores, dataLimite, hoje);
        }

        publicacoes = toDtoList(publicacoesEnt);
        return mapPublicacaoUsuario(user_id, publicacoes);
    }

    public QuantidadePublicacaoDescontoDto contaPublicacaoDesconto (Long user_id){
        verificacao.verificaIdUsuario(user_id);
        UsuarioEntity vendedor = usuarioRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        int quantidadePromocao = publicacaoRepository.countByUsuarioIdAndPromocao(user_id, true);

        return new QuantidadePublicacaoDescontoDto(
                user_id,
                vendedor.getNome(),
                quantidadePromocao
        );
    }

    public ListaPublicacaoDescontoUsuarioDto listaPublicacaoUsuariosDesconto(Long user_id){
        verificacao.verificaIdUsuario(user_id);
        UsuarioEntity vendedor = usuarioRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        List<PublicacaoEntity> publicacoesEnt = publicacaoRepository.findByUsuarioIdAndPromocaoOrderByDataAsc(user_id, true);
        return new ListaPublicacaoDescontoUsuarioDto(
                user_id,
                vendedor.getNome(),
                toDtoList(publicacoesEnt)
        );
    }

    private PublicacaoDto toDto(PublicacaoEntity entity) {
        if (entity == null) return null;

        ProdutosEntity prod = entity.getProduto();
        ProdutoDto produtoDto = null;
        if (prod != null) {
            produtoDto = new ProdutoDto(
                    prod.getNotas(),
                    prod.getCor(),
                    prod.getMarca(),
                    prod.getId(),
                    prod.getTipo(),
                    prod.getNome_produto()
            );
        }

        return new PublicacaoDto(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getData().format(FORMATTER),
                produtoDto,
                entity.getCategoria(),
                entity.getPreco(),
                entity.isPromocao(),
                entity.getDesconto()
        );
    }

    private List<PublicacaoDto> toDtoList(List<PublicacaoEntity> entities) {

        List<PublicacaoDto> dtos = new ArrayList<>();

        if (entities == null) {
            return dtos;
        }

        for (PublicacaoEntity entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }


    private ListaPublicacaoUsuariosDto mapPublicacaoUsuario(long UserId, List<PublicacaoDto> publicacoes ){
        return new ListaPublicacaoUsuariosDto(
            UserId,
            publicacoes
        );
    }

}
