package br.com.mali_social.social_meli.service;


import br.com.mali_social.social_meli.dto.product.ProductDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDiscountUserListDTO;
import br.com.mali_social.social_meli.dto.publish.PublishUserListDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDiscountQuantityDTO;
import br.com.mali_social.social_meli.entity.Product;
import br.com.mali_social.social_meli.entity.Publish;
import br.com.mali_social.social_meli.entity.Follower;
import br.com.mali_social.social_meli.entity.User;
import br.com.mali_social.social_meli.repository.ProductRepository;
import br.com.mali_social.social_meli.repository.PublishRepository;
import br.com.mali_social.social_meli.repository.FollowerRepository;
import br.com.mali_social.social_meli.repository.UserRepository;
import br.com.mali_social.social_meli.util.Verification;
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

public class PublishServiceTest {

    @InjectMocks
    private PublishService publishService;

    @Mock
    private PublishRepository publishRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Verification verification;

    @Mock
    private FollowerRepository followerRepository;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    void testSalvarPublicacaoCorreto(){
        long vendedorId = 10L;
        // ARRANGE
        ProductDTO produto = new ProductDTO(
                "Cadeira", "Edição especial", "Verde", 14l, "Brasil", "cadeira Gamer"
        );

        PublishDTO publicacao = new PublishDTO(
            0l, vendedorId,"08-01-2026", produto, 16, 190.00, false, 0
        );

        Product produtoEnt = new Product(
                produto.getProduct_name(), produto.getColor(), produto.getBrand(), produto.getColor(), produto.getNotes(), produto.getProduct_id()
        );

        User vendedor = new User();
        vendedor.setId(vendedorId);

        Publish publicacaoEnt = new Publish(
                publicacao.getPost_id(), produtoEnt, vendedor, publicacao.isHas_promo() ,publicacao.getDiscount(), publicacao.getPrice(), publicacao.getCategory(), (LocalDate.parse(publicacao.getDate(), formato)));

        when(userRepository.findById(publicacao.getUser_id())).thenReturn(Optional.of(vendedor));
        when(publishRepository.save(any(Publish.class))).thenReturn(publicacaoEnt);

        // ACT
        publishService.salvarPublicacao(publicacao, produtoEnt);

        // ASSERT
        ArgumentCaptor<Publish> captor = ArgumentCaptor.forClass(Publish.class);
        verify(publishRepository, times(1)).save(captor.capture());
        Publish publicacaoSalvo = captor.getValue();
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
        ProductDTO produto = new ProductDTO(
                "Cadeira$", "Edição especial", "Verde", 14l, "Brasil", "cadeira Gamer"
        );

        PublishDTO publicacao = new PublishDTO(
                0l, vendedorId, "08-01-2026", produto, 16, 190.00, false, 0
        );

        Product produtoEnt = new Product(
                produto.getProduct_name(), produto.getColor(), produto.getBrand(), produto.getColor(), produto.getNotes(), produto.getProduct_id()
        );

        User vendedor = new User();
        vendedor.setId(vendedorId);

        Publish publicacaoEnt = new Publish(
                publicacao.getPost_id(), produtoEnt, vendedor, publicacao.isHas_promo(), publicacao.getDiscount(), publicacao.getPrice(), publicacao.getCategory(), (LocalDate.parse(publicacao.getDate(), formato)));

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> publishService.salvarPublicacao(publicacao, produtoEnt)
        );

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testListaPublicacaoUsuariosDtoDescCorreto() {
        LocalDate data;
        long userid = 10L;

        User usuario = new User(userid, "Luis");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Product produtoA = new Product(
                "Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo", 1L
        );
        User usuarioA = new User(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        Publish puba = new Publish(
                1L, produtoA, usuarioA, false, 0, 8000.00, 15, data
        );

        Product produtoB = new Product(
                "Fone de Ouvindo Turbo Max", "Perifericos", "JBL",
                "Preto", "Novo", 2L
        );
        User usuarioB = new User(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        Publish pubb = new Publish(
                2L, produtoB, usuarioB, false, 0, 260.00, 11, data
        );


        Product produtoC = new Product(
                "Garrafa Termica", "Utensilios", "Stanley",
                "Cinza", "1 litro", 3L
        );
        User usuarioC = new User(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        Publish pubc = new Publish(
                3L, produtoC, usuarioC, false, 0, 60.00, 12, data
        );

        Follower relA = new Follower();
        relA.setVendedorId(usuarioA);

        Follower relB = new Follower();
        relB.setVendedorId(usuarioB);

        Follower relC = new Follower();
        relC.setVendedorId(usuarioC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        // ids dos vendedores
        List<Long> idsVendedores = relacoes.stream()
                .map(Follower::getVendedorId)
                .map(User::getId)
                .distinct()
                .toList();

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        // mocks
        when(userRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(followerRepository.findByCompradorId(usuario)).thenReturn(relacoes);
        when(publishRepository.findByUsuarioIdInAndDataBetweenOrderByDataDesc(idsVendedores, dataLimite, hoje)).thenReturn(List.of(pubc, puba, pubb));

        // ACT
        PublishUserListDTO resultado = publishService.listaPublicacaoUsuario(userid, "date_desc");

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

        User usuario = new User(userid, "Luis");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Product produtoA = new Product(
                "Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo", 1L
        );
        User usuarioA = new User(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        Publish puba = new Publish(
                1L, produtoA, usuarioA, false, 0, 8000.00, 15, data
        );

        Product produtoB = new Product(
                "Fone de Ouvindo Turbo Max", "Perifericos", "JBL",
                "Preto", "Novo", 2L
        );
        User usuarioB = new User(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        Publish pubb = new Publish(
                2L, produtoB, usuarioB, false, 0, 260.00, 11, data
        );


        Product produtoC = new Product(
                "Garrafa Termica", "Utensilios", "Stanley",
                "Cinza", "1 litro", 3L
        );
        User usuarioC = new User(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        Publish pubc = new Publish(
                3L, produtoC, usuarioC, false, 0, 60.00, 12, data
        );

        Follower relA = new Follower();
        relA.setVendedorId(usuarioA);

        Follower relB = new Follower();
        relB.setVendedorId(usuarioB);

        Follower relC = new Follower();
        relC.setVendedorId(usuarioC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        List<Long> idsVendedores = relacoes.stream()
                .map(Follower::getVendedorId)
                .map(User::getId)
                .distinct()
                .toList();

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        // mocks
        when(userRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(followerRepository.findByCompradorId(usuario)).thenReturn(relacoes);
        when(publishRepository.findByUsuarioIdInAndDataBetweenOrderByDataAsc(idsVendedores, dataLimite, hoje)).thenReturn(List.of(pubb, puba, pubc));

        // ACT
        PublishUserListDTO resultado = publishService.listaPublicacaoUsuario(userid, "date_asc");

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
        User vendedor = new User(
                vendedor_id,
                "Luiz"
        );

        when(userRepository.findById(vendedor_id)).thenReturn(Optional.of(vendedor));
        when(publishRepository.countByUsuarioIdAndPromocao(vendedor_id,true)).thenReturn(QtdPromocao);

        PublishDiscountQuantityDTO QtdPublicacaoDesconto = publishService.contaPublicacaoDesconto(vendedor_id);

        assertEquals(QtdPromocao, QtdPublicacaoDesconto.getPromo_products_count());
        assertEquals(vendedor.getId(), QtdPublicacaoDesconto.getUser_id());
        assertEquals(vendedor.getNome(), QtdPublicacaoDesconto.getUser_name());

    }

    @Test
    void TestlistaPublicacaoComDescontoCorreto() {
        LocalDate data;
        long userid = 10L;

        User usuario = new User(userid, "Luis");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Product produtoA = new Product(
                "Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo", 1L
        );
        User usuarioA = new User(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        Publish puba = new Publish(
                1L, produtoA, usuarioA, true, 0, 8000.00, 15, data
        );

        Product produtoB = new Product(
                "Fone de Ouvindo Turbo Max", "Perifericos", "JBL",
                "Preto", "Novo", 2L
        );
        User usuarioB = new User(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        Publish pubb = new Publish(
                2L, produtoB, usuarioB, false, 0, 260.00, 11, data
        );


        Product produtoC = new Product(
                "Garrafa Termica", "Utensilios", "Stanley",
                "Cinza", "1 litro", 3L
        );
        User usuarioC = new User(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        Publish pubc = new Publish(
                3L, produtoC, usuarioC, true, 0, 60.00, 12, data
        );

        // mocks
        when(userRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(publishRepository.findByUsuarioIdAndPromocaoOrderByDataAsc(userid,true)).thenReturn(List.of(puba, pubc));

        // ACT
        PublishDiscountUserListDTO ProdutoPromocao = publishService.listaPublicacaoUsuariosDesconto(userid);

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

        User comprador = new User();
        comprador.setId(userId);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(comprador));

        User vendedor1 = new User();
        vendedor1.setId(10L);
        User vendedor2 = new User();
        vendedor2.setId(20L);

        Follower seg1 = new Follower();
        seg1.setVendedorId(vendedor1);
        seg1.setCompradorId(comprador);

        Follower seg2 = new Follower();
        seg2.setVendedorId(vendedor2);
        seg2.setCompradorId(comprador);

        when(followerRepository.findByCompradorId(comprador))
                .thenReturn(List.of(seg1, seg2));

        LocalDate hojeEsperado = LocalDate.of(2026, 1, 15);
        LocalDate dataLimiteEsperada = hojeEsperado.minusWeeks(2);


        Publish pub1 = new Publish();
        pub1.setUsuario(vendedor1);
        pub1.setData(hojeEsperado.minusDays(1));

        Publish pub2 = new Publish();
        pub2.setUsuario(vendedor2);
        pub2.setData(hojeEsperado.minusDays(5));

        when(publishRepository
                .findByUsuarioIdInAndDataBetweenOrderByDataDesc(
                        anyList(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(pub1, pub2));

        // Act
        PublishUserListDTO resultado =
                publishService.listaPublicacaoUsuario(userId, "date_desc");

        // Assert

        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass((Class) List.class);
        ArgumentCaptor<LocalDate> dataInicioCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> dataFimCaptor = ArgumentCaptor.forClass(LocalDate.class);

        verify(publishRepository).findByUsuarioIdInAndDataBetweenOrderByDataDesc(
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
