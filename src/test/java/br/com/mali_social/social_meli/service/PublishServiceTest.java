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
                produto.getProduct_id(), produto.getColor(), produto.getNotes(), produto.getBrand(), produto.getType() ,produto.getProduct_name()
        );

        User vendedor = new User();
        vendedor.setId(vendedorId);

        Publish publicacaoEnt = new Publish(
                publicacao.getPost_id(), publicacao.getCategory() , (LocalDate.parse(publicacao.getDate(), formato)) , publicacao.getPrice(), publicacao.isHas_promo() ,produtoEnt, publicacao.getDiscount(), vendedor);

        when(userRepository.findById(publicacao.getUser_id())).thenReturn(Optional.of(vendedor));
        when(publishRepository.save(any(Publish.class))).thenReturn(publicacaoEnt);

        // ACT
        publishService.savePublish(publicacao, produtoEnt);

        // ASSERT
        ArgumentCaptor<Publish> captor = ArgumentCaptor.forClass(Publish.class);
        verify(publishRepository, times(1)).save(captor.capture());
        Publish publicacaoSalvo = captor.getValue();
        assertEquals(10L, publicacaoSalvo.getUser().getId());
        assertEquals(LocalDate.parse("08-01-2026", formato), publicacaoSalvo.getDate());
        assertEquals(16, publicacaoSalvo.getCategory());
        assertEquals(190.00, publicacaoSalvo.getPrice());
        assertEquals(false, publicacaoSalvo.isHas_promo());
        assertEquals(0, publicacaoSalvo.getDiscount());
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
                produto.getProduct_id(), produto.getColor(), produto.getNotes(), produto.getBrand(), produto.getType() ,produto.getProduct_name()
        );

        User vendedor = new User();
        vendedor.setId(vendedorId);

        Publish publicacaoEnt = new Publish(
                publicacao.getPost_id(), publicacao.getCategory() , (LocalDate.parse(publicacao.getDate(), formato)) , publicacao.getPrice(), publicacao.isHas_promo() ,produtoEnt, publicacao.getDiscount(), vendedor);

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> publishService.savePublish(publicacao, produtoEnt)
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
                1L,"Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo"
        );
        User usuarioA = new User(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        Publish puba = new Publish(
                1L, 15 ,data,8000.00, true, produtoA, 0 ,usuarioA);

        Product produtoB = new Product(
                2L,"Preto","Novo","JBL", "Perifericos","Fone de Ouvindo Turbo Max"
        );
        User usuarioB = new User(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        Publish pubb = new Publish(
                2l, 11, data, 260.00, true, produtoB,0,usuarioB
        );


        Product produtoC = new Product(
                3L,"Cinza","1 Litro","Stanley", "Utensilios","Garrafa Termica"
        );

        User usuarioC = new User(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        Publish pubc = new Publish(
                2L, 12,data,60.00,true,produtoC, 0 ,usuarioC
        );

        Follower relA = new Follower();
        relA.setSellerId(usuarioA);

        Follower relB = new Follower();
        relB.setSellerId(usuarioB);

        Follower relC = new Follower();
        relC.setSellerId(usuarioC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        // ids dos vendedores
        List<Long> idsVendedores = relacoes.stream()
                .map(Follower::getSellerId)
                .map(User::getId)
                .distinct()
                .toList();

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        // mocks
        when(userRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(followerRepository.findByBuyerId(usuario)).thenReturn(relacoes);
        when(publishRepository.findByUserIdInAndDateBetweenOrderByDateDesc(idsVendedores, dataLimite, hoje)).thenReturn(List.of(pubc, puba, pubb));

        // ACT
        PublishUserListDTO resultado = publishService.listPublishUser(userid, "date_desc");

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
                1L,"Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo"
        );
        User usuarioA = new User(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        Publish puba = new Publish(
                1L, 15 ,data,8000.00, false, produtoA, 0 ,usuarioA);

        Product produtoB = new Product(
                2L,"Preto","Novo","JBL", "Perifericos","Fone de Ouvindo Turbo Max"
        );
        User usuarioB = new User(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        Publish pubb = new Publish(
                2l, 11, data, 260.00, false, produtoB,0,usuarioB
        );


        Product produtoC = new Product(
                3L,"Cinza","1 Litro","Stanley", "Utensilios","Garrafa Termica"
        );

        User usuarioC = new User(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        Publish pubc = new Publish(
                2L, 12,data,60.00,true,produtoC, 0 ,usuarioC
        );


        Follower relA = new Follower();
        relA.setSellerId(usuarioA);

        Follower relB = new Follower();
        relB.setSellerId(usuarioB);

        Follower relC = new Follower();
        relC.setSellerId(usuarioC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        List<Long> idsVendedores = relacoes.stream()
                .map(Follower::getSellerId)
                .map(User::getId)
                .distinct()
                .toList();

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusWeeks(2);

        // mocks
        when(userRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(followerRepository.findByBuyerId(usuario)).thenReturn(relacoes);
        when(publishRepository.findByUserIdInAndDateBetweenOrderByDateAsc(idsVendedores, dataLimite, hoje)).thenReturn(List.of(pubb, puba, pubc));

        // ACT
        PublishUserListDTO resultado = publishService.listPublishUser(userid, "date_asc");

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
        when(publishRepository.countByUserIdAndHasPromo(vendedor_id,true)).thenReturn(QtdPromocao);

        PublishDiscountQuantityDTO QtdPublicacaoDesconto = publishService.countPublishDiscount(vendedor_id);

        assertEquals(QtdPromocao, QtdPublicacaoDesconto.getPromo_products_count());
        assertEquals(vendedor.getId(), QtdPublicacaoDesconto.getUser_id());
        assertEquals(vendedor.getUserName(), QtdPublicacaoDesconto.getUser_name());

    }

    @Test
    void testlistaPublicacaoComDescontoCorreto (){
        Long userid = 1L;
        User usuario = new User(userid, "Gustavo");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate data;
        Product produtoA = new Product(
                1L,"Iphone 15 PRO", "Smartphone", "Apple",
                "Branco", "Seminovo"
        );
        User usuarioA = new User(14L, "João");
        data = LocalDate.parse("10-01-2026", formatter);
        Publish puba = new Publish(
                1L, 15 ,data,8000.00, true, produtoA, 0 ,usuarioA);

        Product produtoB = new Product(
                2L,"Preto","Novo","JBL", "Perifericos","Fone de Ouvindo Turbo Max"
        );
        User usuarioB = new User(15L, "Julio");
        data = LocalDate.parse("01-01-2026", formatter);
        Publish pubb = new Publish(
                2l, 11, data, 260.00, true, produtoB,0,usuarioB
        );


        Product produtoC = new Product(
                3L,"Cinza","1 Litro","Stanley", "Utensilios","Garrafa Termica"
        );

        User usuarioC = new User(16L, "Maria");
        data = LocalDate.parse("16-01-2026", formatter);
        Publish pubc = new Publish(
                2L, 12,data,60.00,true,produtoC, 0 ,usuarioC
        );


        // mocks
        when(userRepository.findById(userid)).thenReturn(Optional.of(usuario));
        when(publishRepository.findByUserIdAndHasPromoOrderByDateAsc(userid,true)).thenReturn(List.of(puba, pubc));

        // ACT
        PublishDiscountUserListDTO ProdutoPromocao = publishService.listPublishUserDiscount(userid);

        // ASSERT
        assertNotNull(ProdutoPromocao);
        assertEquals(userid, ProdutoPromocao.getUser_id());
        assertEquals(2, ProdutoPromocao.getPosts().size());
        assertTrue(ProdutoPromocao.getPosts().get(0).isHas_promo());
        assertTrue(ProdutoPromocao.getPosts().get(1).isHas_promo());
    }

    @Test
    void testdeveBuscarPublicacoesDasDuasUltimasSemanasOrdenadasPorDataDesc() {
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
        seg1.setSellerId(vendedor1);
        seg1.setBuyerId(comprador);

        Follower seg2 = new Follower();
        seg2.setSellerId(vendedor2);
        seg2.setBuyerId(comprador);

        when(followerRepository.findByBuyerId(comprador))
                .thenReturn(List.of(seg1, seg2));

        LocalDate hojeEsperado = LocalDate.now();
        LocalDate dataLimiteEsperada = hojeEsperado.minusWeeks(2);


        Publish pub1 = new Publish();
        pub1.setUser(vendedor1);
        pub1.setDate(hojeEsperado.minusDays(1));

        Publish pub2 = new Publish();
        pub2.setUser(vendedor2);
        pub2.setDate(hojeEsperado.minusDays(5));

        when(publishRepository
                .findByUserIdInAndDateBetweenOrderByDateDesc(
                        anyList(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(pub1, pub2));

        // Act
        PublishUserListDTO resultado =
                publishService.listPublishUser(userId, "date_desc");

        // Assert

        ArgumentCaptor<List<Long>> idsCaptor = ArgumentCaptor.forClass((Class) List.class);
        ArgumentCaptor<LocalDate> dataInicioCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> dataFimCaptor = ArgumentCaptor.forClass(LocalDate.class);

        verify(publishRepository).findByUserIdInAndDateBetweenOrderByDateDesc(
                idsCaptor.capture(),
                dataInicioCaptor.capture(),
                dataFimCaptor.capture()
        );


        List<Long> idsPassados = idsCaptor.getValue();
        assertEquals(2, idsPassados.size());

        assertEquals(dataLimiteEsperada, dataInicioCaptor.getValue());
        assertEquals(hojeEsperado, dataFimCaptor.getValue());

        assertNotNull(resultado);
    }
}
