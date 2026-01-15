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
import br.com.mali_social.social_meli.repository.ProdutoRepository;
import br.com.mali_social.social_meli.repository.PublicacaoRepository;
import br.com.mali_social.social_meli.repository.SeguidoresRepository;
import br.com.mali_social.social_meli.repository.UsuarioRepository;
import br.com.mali_social.social_meli.util.Verificacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class PublicacaoServiceTest {

    @InjectMocks
    private PublicacaoService publicacaoService;

    @Mock
    private PublicacaoRepository publicacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private Verificacao verificacao;

    @Mock
    private SeguidoresRepository seguidoresRepository;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    void testSalvarPublicacaoCorreto(){
        long vendedorId = 10L;
        // ARRANGE
        ProdutoDto produto = new ProdutoDto(
                "Cadeira", "Edição especial", "Verde", 14l, "Brasil", "cadeira Gamer"
        );

        PublicacaoDto publicacao = new PublicacaoDto(
            0l, vendedorId,"08-01-2026", produto, 16, 190.00, false, 0
        );

        ProdutosEntity produtoEnt = new ProdutosEntity(
                produto.getProduct_name(), produto.getColor(), produto.getBrand(), produto.getColor(), produto.getNotes(), produto.getProduct_id()
        );

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        PublicacaoEntity publicacaoEnt = new PublicacaoEntity(
                publicacao.getPost_id(), produtoEnt, vendedor, publicacao.isHas_promo() ,publicacao.getDiscount(), publicacao.getPrice(), publicacao.getCategory(), (LocalDate.parse(publicacao.getDate(), formato)));

        when(usuarioRepository.findById(publicacao.getUser_id())).thenReturn(Optional.of(vendedor));
        when(publicacaoRepository.save(any(PublicacaoEntity.class))).thenReturn(publicacaoEnt);

        // ACT
        publicacaoService.salvarPublicacao(publicacao, produtoEnt);

        // ASSERT
        ArgumentCaptor<PublicacaoEntity> captor = ArgumentCaptor.forClass(PublicacaoEntity.class);
        verify(publicacaoRepository, times(1)).save(captor.capture());
        PublicacaoEntity publicacaoSalvo = captor.getValue();
        assertEquals(10L, publicacaoSalvo.getUsuario().getId());
        assertEquals(LocalDate.parse("08-01-2026", formato), publicacaoSalvo.getData());
        assertEquals(16, publicacaoSalvo.getCategoria());
        assertEquals(190.00, publicacaoSalvo.getPreco());
        assertEquals(false, publicacaoSalvo.isPromocao());
        assertEquals(0, publicacaoSalvo.getDesconto());
    }

    @Test
    void testSalvarPublicacaoErroCadastro() {
        long vendedorId = 10L;
        // ARRANGE
        ProdutoDto produto = new ProdutoDto(
                "Cadeira$", "Edição especial", "Verde", 14l, "Brasil", "cadeira Gamer"
        );

        PublicacaoDto publicacao = new PublicacaoDto(
                0l, vendedorId, "08-01-2026", produto, 16, 190.00, false, 0
        );

        ProdutosEntity produtoEnt = new ProdutosEntity(
                produto.getProduct_name(), produto.getColor(), produto.getBrand(), produto.getColor(), produto.getNotes(), produto.getProduct_id()
        );

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        PublicacaoEntity publicacaoEnt = new PublicacaoEntity(
                publicacao.getPost_id(), produtoEnt, vendedor, publicacao.isHas_promo(), publicacao.getDiscount(), publicacao.getPrice(), publicacao.getCategory(), (LocalDate.parse(publicacao.getDate(), formato)));

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> publicacaoService.salvarPublicacao(publicacao, produtoEnt)
        );

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testListaPublicacaoUsuariosDtoDescCorreto() {
        LocalDate data;
        long userid = 10L;

        UsuarioEntity usuario = new UsuarioEntity(userid, "Luis");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ProdutosEntity produtoA = new ProdutosEntity(
                "Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo", 1L
        );
        UsuarioEntity usuarioA = new UsuarioEntity(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        PublicacaoEntity puba = new PublicacaoEntity(
                1L, produtoA, usuarioA, false, 0, 8000.00, 15, data
        );

        ProdutosEntity produtoB = new ProdutosEntity(
                "Fone de Ouvindo Turbo Max", "Perifericos", "JBL",
                "Preto", "Novo", 2L
        );
        UsuarioEntity usuarioB = new UsuarioEntity(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        PublicacaoEntity pubb = new PublicacaoEntity(
                2L, produtoB, usuarioB, false, 0, 260.00, 11, data
        );


        ProdutosEntity produtoC = new ProdutosEntity(
                "Garrafa Termica", "Utensilios", "Stanley",
                "Cinza", "1 litro", 3L
        );
        UsuarioEntity usuarioC = new UsuarioEntity(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        PublicacaoEntity pubc = new PublicacaoEntity(
                3L, produtoC, usuarioC, false, 0, 60.00, 12, data
        );

        SeguidoresEntity relA = new SeguidoresEntity();
        relA.setVendedorId(usuarioA);

        SeguidoresEntity relB = new SeguidoresEntity();
        relB.setVendedorId(usuarioB);

        SeguidoresEntity relC = new SeguidoresEntity();
        relC.setVendedorId(usuarioC);

        List<SeguidoresEntity> relacoes = List.of(relA, relB, relC);

        // ids dos vendedores
        List<Long> idsVendedores = relacoes.stream()
                .map(SeguidoresEntity::getVendedorId)
                .map(UsuarioEntity::getId)
                .distinct()
                .toList();

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        // mocks
        when(usuarioRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(seguidoresRepository.findByCompradorId(usuario)).thenReturn(relacoes);
        when(publicacaoRepository.findByUsuarioIdInAndDataBetweenOrderByDataDesc(idsVendedores, dataLimite, hoje)).thenReturn(List.of(pubc, puba, pubb));

        // ACT
        ListaPublicacaoUsuariosDto resultado = publicacaoService.listaPublicacaoUsuario(userid, "date_desc");

        // ASSERT
        assertNotNull(resultado);
        assertEquals(userid, resultado.getUser_id());
        assertEquals(3, resultado.getPublicacoes().size());


        assertEquals("16-01-2026", resultado.getPublicacoes().get(0).getDate());
        assertEquals("10-01-2026", resultado.getPublicacoes().get(1).getDate());
        assertEquals("01-01-2026", resultado.getPublicacoes().get(2).getDate());
    }

    @Test
    void testListaPublicacaoUsuariosDtoAscCorreto() {
        LocalDate data;
        long userid = 10L;

        UsuarioEntity usuario = new UsuarioEntity(userid, "Luis");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ProdutosEntity produtoA = new ProdutosEntity(
                "Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo", 1L
        );
        UsuarioEntity usuarioA = new UsuarioEntity(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        PublicacaoEntity puba = new PublicacaoEntity(
                1L, produtoA, usuarioA, false, 0, 8000.00, 15, data
        );

        ProdutosEntity produtoB = new ProdutosEntity(
                "Fone de Ouvindo Turbo Max", "Perifericos", "JBL",
                "Preto", "Novo", 2L
        );
        UsuarioEntity usuarioB = new UsuarioEntity(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        PublicacaoEntity pubb = new PublicacaoEntity(
                2L, produtoB, usuarioB, false, 0, 260.00, 11, data
        );


        ProdutosEntity produtoC = new ProdutosEntity(
                "Garrafa Termica", "Utensilios", "Stanley",
                "Cinza", "1 litro", 3L
        );
        UsuarioEntity usuarioC = new UsuarioEntity(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        PublicacaoEntity pubc = new PublicacaoEntity(
                3L, produtoC, usuarioC, false, 0, 60.00, 12, data
        );

        SeguidoresEntity relA = new SeguidoresEntity();
        relA.setVendedorId(usuarioA);

        SeguidoresEntity relB = new SeguidoresEntity();
        relB.setVendedorId(usuarioB);

        SeguidoresEntity relC = new SeguidoresEntity();
        relC.setVendedorId(usuarioC);

        List<SeguidoresEntity> relacoes = List.of(relA, relB, relC);

        List<Long> idsVendedores = relacoes.stream()
                .map(SeguidoresEntity::getVendedorId)
                .map(UsuarioEntity::getId)
                .distinct()
                .toList();

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        // mocks
        when(usuarioRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(seguidoresRepository.findByCompradorId(usuario)).thenReturn(relacoes);
        when(publicacaoRepository.findByUsuarioIdInAndDataBetweenOrderByDataAsc(idsVendedores, dataLimite, hoje)).thenReturn(List.of(pubb, puba, pubc));

        // ACT
        ListaPublicacaoUsuariosDto resultado = publicacaoService.listaPublicacaoUsuario(userid, "date_asc");

        // ASSERT
        assertNotNull(resultado);
        assertEquals(userid, resultado.getUser_id());
        assertEquals(3, resultado.getPublicacoes().size());


        assertEquals("01-01-2026", resultado.getPublicacoes().get(0).getDate());
        assertEquals("10-01-2026", resultado.getPublicacoes().get(1).getDate());
        assertEquals("16-01-2026", resultado.getPublicacoes().get(2).getDate());
    }

    @Test
    void testContarPublicacaoPromocaoCorreto(){
        Long vendedor_id = 17L;
        int QtdPromocao = 20;
        UsuarioEntity vendedor = new UsuarioEntity(
                vendedor_id,
                "Luiz"
        );

        when(usuarioRepository.findById(vendedor_id)).thenReturn(Optional.of(vendedor));
        when(publicacaoRepository.countByUsuarioIdAndPromocao(vendedor_id,true)).thenReturn(QtdPromocao);

        QuantidadePublicacaoDescontoDto QtdPublicacaoDesconto = publicacaoService.contaPublicacaoDesconto(vendedor_id);

        assertEquals(QtdPromocao, QtdPublicacaoDesconto.getPromo_products_count());
        assertEquals(vendedor.getId(), QtdPublicacaoDesconto.getUser_id());
        assertEquals(vendedor.getNome(), QtdPublicacaoDesconto.getUser_name());

    }

    @Test
    void TestlistaPublicacaoComDescontoCorreto() {
        LocalDate data;
        long userid = 10L;

        UsuarioEntity usuario = new UsuarioEntity(userid, "Luis");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ProdutosEntity produtoA = new ProdutosEntity(
                "Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo", 1L
        );
        UsuarioEntity usuarioA = new UsuarioEntity(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        PublicacaoEntity puba = new PublicacaoEntity(
                1L, produtoA, usuarioA, true, 0, 8000.00, 15, data
        );

        ProdutosEntity produtoB = new ProdutosEntity(
                "Fone de Ouvindo Turbo Max", "Perifericos", "JBL",
                "Preto", "Novo", 2L
        );
        UsuarioEntity usuarioB = new UsuarioEntity(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        PublicacaoEntity pubb = new PublicacaoEntity(
                2L, produtoB, usuarioB, false, 0, 260.00, 11, data
        );


        ProdutosEntity produtoC = new ProdutosEntity(
                "Garrafa Termica", "Utensilios", "Stanley",
                "Cinza", "1 litro", 3L
        );
        UsuarioEntity usuarioC = new UsuarioEntity(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        PublicacaoEntity pubc = new PublicacaoEntity(
                3L, produtoC, usuarioC, true, 0, 60.00, 12, data
        );

        // mocks
        when(usuarioRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(publicacaoRepository.findByUsuarioIdAndPromocaoOrderByDataAsc(userid,true)).thenReturn(List.of(puba, pubc));

        // ACT
        ListaPublicacaoDescontoUsuarioDto ProdutoPromocao = publicacaoService.listaPublicacaoUsuariosDesconto(userid);

        // ASSERT
        assertNotNull(ProdutoPromocao);
        assertEquals(userid, ProdutoPromocao.getUser_id());
        assertEquals(2, ProdutoPromocao.getPosts().size());
        assertTrue(ProdutoPromocao.getPosts().get(0).isHas_promo());
        assertTrue(ProdutoPromocao.getPosts().get(1).isHas_promo());
    }

    @Test
    void TestdeveBuscarPublicacoesDasDuasUltimasSemanasOrdenadasPorDataDesc() {
        Long userId = 1L;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(userId);

        when(usuarioRepository.findById(userId))
                .thenReturn(Optional.of(comprador));

        UsuarioEntity vendedor1 = new UsuarioEntity();
        vendedor1.setId(10L);
        UsuarioEntity vendedor2 = new UsuarioEntity();
        vendedor2.setId(20L);

        SeguidoresEntity seg1 = new SeguidoresEntity();
        seg1.setVendedorId(vendedor1);
        seg1.setCompradorId(comprador);

        SeguidoresEntity seg2 = new SeguidoresEntity();
        seg2.setVendedorId(vendedor2);
        seg2.setCompradorId(comprador);

        when(seguidoresRepository.findByCompradorId(comprador))
                .thenReturn(List.of(seg1, seg2));

        LocalDate hojeEsperado = LocalDate.of(2026, 1, 15);
        LocalDate dataLimiteEsperada = hojeEsperado.minusWeeks(2);


        PublicacaoEntity pub1 = new PublicacaoEntity();
        pub1.setUsuario(vendedor1);
        pub1.setData(hojeEsperado.minusDays(1));

        PublicacaoEntity pub2 = new PublicacaoEntity();
        pub2.setUsuario(vendedor2);
        pub2.setData(hojeEsperado.minusDays(5));

        when(publicacaoRepository
                .findByUsuarioIdInAndDataBetweenOrderByDataDesc(
                        anyList(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(pub1, pub2));

        // Act
        ListaPublicacaoUsuariosDto resultado =
                publicacaoService.listaPublicacaoUsuario(userId, "date_desc");

        // Assert

        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass((Class) List.class);
        ArgumentCaptor<LocalDate> dataInicioCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> dataFimCaptor = ArgumentCaptor.forClass(LocalDate.class);

        verify(publicacaoRepository).findByUsuarioIdInAndDataBetweenOrderByDataDesc(
                idsCaptor.capture(),
                dataInicioCaptor.capture(),
                dataFimCaptor.capture()
        );


        List<Long> idsPassados = idsCaptor.getValue();
        assertTrue(idsPassados.contains(10L));
        assertTrue(idsPassados.contains(20L));
        assertEquals(2, idsPassados.size());

        assertEquals(dataLimiteEsperada, dataInicioCaptor.getValue());
        assertEquals(hojeEsperado, dataFimCaptor.getValue());

        assertNotNull(resultado);
    }
}
