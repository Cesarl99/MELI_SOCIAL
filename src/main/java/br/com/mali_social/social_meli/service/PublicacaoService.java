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
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class PublicacaoService {
    private final PublicacaoRepository publicacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeguidoresRepository seguidoresRepository;
    private final ProdutoService produtoService;
    private final Verificacao verificacao = new Verificacao();

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PublicacaoService (PublicacaoRepository publicacaoRepository, UsuarioRepository usuarioRepository, SeguidoresRepository seguidoresRepository, ProdutoService produtoService){
        this.publicacaoRepository = publicacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.seguidoresRepository = seguidoresRepository;
        this.produtoService = produtoService;
    }

    public String salvarPublicacao(PublicacaoDto publicacaoDto, ProdutosEntity produtosEntity){
        PublicacaoEntity publicacao = new PublicacaoEntity();
        String msgErro = verificacao.verificaCadastroProduto(publicacaoDto);
        if(msgErro.isEmpty()){

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
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msgErro);
    }

    public ListaPublicacaoUsuariosDto ListaPublicacaoUsuario(Long UserId, String order) {
        List<SeguidoresEntity> relacao = seguidoresRepository.findByCompradorId(UserId);

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        List<Long> idsVendedores = relacao.stream()
                .map(SeguidoresEntity::getVendedor_id)
                .distinct()
                .toList();

        List<PublicacaoEntity> publicacoesEnt = new ArrayList<>();
        List<PublicacaoDto> publicacoes = new ArrayList<>();

        if ("date_desc".equals(order)){
            System.out.println("date_desc");
            publicacoesEnt = publicacaoRepository.findByUsuarioIdInAndDataBetweenOrderByDataDesc(idsVendedores,dataLimite, hoje);
        } else {
            System.out.println("date_asc");
            publicacoesEnt = publicacaoRepository.findByUsuarioIdInAndDataBetweenOrderByDataAsc(idsVendedores, dataLimite, hoje);
        }

        System.out.println("VALOR DO publicacaoEnt " + publicacoesEnt );

        publicacoes = toDtoList(publicacoesEnt);

        return mapPublicacaoUsuario(UserId, publicacoes);
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
                    prod.getTipo(),
                    prod.getNome_produto(),
                    prod.getId()
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

    public List<PublicacaoDto> toDtoList(List<PublicacaoEntity> entities) {

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
