package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.follower.UserFollowerListDTO;
import br.com.mali_social.social_meli.dto.follower.UserFollowersListDTO;
import br.com.mali_social.social_meli.dto.follower.FollowersQuantityDTO;
import br.com.mali_social.social_meli.dto.user.UserDTO;
import br.com.mali_social.social_meli.entity.Follower;
import br.com.mali_social.social_meli.entity.User;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private FollowerRepository followerRepository;

    @Mock
    private Verification verification;

    @Test
    void testSalvaUsuarioSucesso() {
        // ARRANGE
        UserDTO usuariodtoTest = new UserDTO();
        usuariodtoTest.setUser_name("Luis");

        // ACT
        userService.saveUser(usuariodtoTest);

        // ASSERT
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());

        User entidadeQueFoiSalva = captor.getValue();
        assertEquals("Luis", entidadeQueFoiSalva.getUserName());
    }

    @Test
    void testSeguirComSucesso() {
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        User comprador = new User();
        comprador.setId(compradorId);

        User vendedor = new User();
        vendedor.setId(vendedorId);

        Follower salvo = new Follower();
        salvo.setBuyerId(comprador);
        salvo.setSellerId(vendedor);

        when(userRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(userRepository.findById(compradorId)).thenReturn(Optional.of(comprador));
        when(followerRepository.save(any(Follower.class))).thenReturn(salvo);

        // ACT
        userService.follow(compradorId, vendedorId);

        // ASSERT
        ArgumentCaptor<Follower> segCaptor = ArgumentCaptor.forClass(Follower.class);
        verify(followerRepository).save(segCaptor.capture());
        Follower enviadoParaSalvar = segCaptor.getValue();

        assertEquals(comprador, enviadoParaSalvar.getBuyerId());
        assertEquals(vendedor, enviadoParaSalvar.getSellerId());
    }

    @Test
    void testSeguirExececaoCompradorNaoCadastrado(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        User comprador = new User();
        comprador.setId(compradorId);

        User vendedor = new User();
        vendedor.setId(vendedorId);

        when(userRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(userRepository.findById(compradorId)).thenReturn(Optional.empty());

        // ACT
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.follow(compradorId, vendedorId)
        );
        // ASSERT
        assertEquals("O usuario (comprador) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSeguirExcecaoVendedorNaoCadastro(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        User comprador = new User();
        comprador.setId(compradorId);

        User vendedor = new User();
        vendedor.setId(vendedorId);
        when(userRepository.findById(vendedorId)).thenReturn(Optional.empty());

        // ACT
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.follow(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O usuario (vendedor) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testSeguirExcecaoIdUsuarioInvalido(){
        // ARRANGE
        long compradorId = 0;
        long vendedorId = 0;

        User comprador = new User();
        comprador.setId(compradorId);

        User vendedor = new User();
        vendedor.setId(vendedorId);

        // ACT
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.follow(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

    }

    @Test
    void testContarSeguidoresCorreto(){
        // arrange
        long vendedorId = 14;

        User vendedor = new User();

        vendedor.setId(vendedorId);
        when(userRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(followerRepository.countBySellerId(vendedor)).thenReturn(25);

        // act
        FollowersQuantityDTO seguidores = userService.countFollowers(vendedorId);

        assertNotNull(seguidores);

        assertEquals(25, seguidores.getFollowersCount());
    }

    @Test
    void testContarSeguidoresExcecaoUsuarioNaoCadastrado(){

        // arrange
        long vendedorId = 14;

        User vendedor = new User();

        vendedor.setId(vendedorId);
        when(userRepository.findById(vendedorId)).thenReturn(Optional.empty());


        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.countFollowers(vendedorId)
        );


        // ASSERT
        assertEquals("O usuario não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testContarSeguidoresExcecaoIdInvalido(){

        // arrange
        long vendedorId = 0;

        User vendedor = new User();

        vendedor.setId(vendedorId);


        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.countFollowers(vendedorId)
        );


        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testListaSeguidoresDeveRetornarOrdenadoPorNomeAsc() {
        // ARRANGE
        long vendedorId = 5;

        User vendedor = new User();
        vendedor.setId(vendedorId);

        User segA = new User();
        segA.setId(1l);
        segA.setUserName("Ana");

        User segB = new User();
        segB.setId(2l);
        segB.setUserName("Carlos");

        User segC = new User();
        segC.setId(3l);
        segC.setUserName("Bruno");

        Follower relA = new Follower();
        relA.setBuyerId(segA);

        Follower relB = new Follower();
        relB.setBuyerId(segB);

        Follower relC = new Follower();
        relC.setBuyerId(segC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        when(userRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(followerRepository.findBySellerId(vendedor)).thenReturn(relacoes);
        when(userRepository.findById(relacoes.get(0).getBuyerId().getId())).thenReturn(Optional.of(relacoes.get(0).getBuyerId()));
        when(userRepository.findById(relacoes.get(1).getBuyerId().getId())).thenReturn(Optional.of(relacoes.get(1).getBuyerId()));
        when(userRepository.findById(relacoes.get(2).getBuyerId().getId())).thenReturn(Optional.of(relacoes.get(2).getBuyerId()));


        // ACT
        UserFollowersListDTO dto = userService.listFollowers(vendedorId, "name_asc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguidores());

        List<UserDTO> seguidores = dto.getSeguidores();

        assertEquals(3, seguidores.size());
        assertEquals("Ana", seguidores.get(0).getUser_name());
        assertEquals("Bruno", seguidores.get(1).getUser_name());
        assertEquals("Carlos", seguidores.get(2).getUser_name());
    }

    @Test
    void testListaSeguidoresDeveRetornarOrdenadoPorNomeDesc() {
        // ARRANGE
        long vendedorId = 5;

        User vendedor = new User();
        vendedor.setId(vendedorId);

        User segA = new User();
        segA.setId(1l);
        segA.setUserName("Ana");

        User segB = new User();
        segB.setId(2l);
        segB.setUserName("Carlos");

        User segC = new User();
        segC.setId(3l);
        segC.setUserName("Bruno");

        Follower relA = new Follower();
        relA.setBuyerId(segA);

        Follower relB = new Follower();
        relB.setBuyerId(segB);

        Follower relC = new Follower();
        relC.setBuyerId(segC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        when(userRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(followerRepository.findBySellerId(vendedor)).thenReturn(relacoes);
        when(userRepository.findById(relacoes.get(0).getBuyerId().getId())).thenReturn(Optional.of(relacoes.get(0).getBuyerId()));
        when(userRepository.findById(relacoes.get(1).getBuyerId().getId())).thenReturn(Optional.of(relacoes.get(1).getBuyerId()));
        when(userRepository.findById(relacoes.get(2).getBuyerId().getId())).thenReturn(Optional.of(relacoes.get(2).getBuyerId()));


        // ACT
        UserFollowersListDTO dto = userService.listFollowers(vendedorId, "name_desc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguidores());

        List<UserDTO> seguidores = dto.getSeguidores();

        assertEquals(3, seguidores.size());
        assertEquals("Carlos", seguidores.get(0).getUser_name());
        assertEquals("Bruno", seguidores.get(1).getUser_name());
        assertEquals("Ana", seguidores.get(2).getUser_name());
    }

    @Test
    void testListaSeguidoresUsuarioNaoCadastro(){

        // arrange
        long vendedorId = 14;

        User vendedor = new User();

        vendedor.setId(vendedorId);
        when(userRepository.findById(vendedorId)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.listFollowed(vendedorId, "name_desc")
        );

        // ASSERT
        assertEquals("O usuario não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testListaSeguidoresExcecaoUsuarioInvalido(){

        // arrange
        long vendedorId = 0;

        User vendedor = new User();

        vendedor.setId(vendedorId);

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.listFollowed(vendedorId, "name_desc")
        );


        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testListaSeguindoDeveRetornarOrdenadoPorNomeAsc() {
        // ARRANGE
        long compradorId = 5;

        User comprador = new User();
        comprador.setId(compradorId);

        User segA = new User();
        segA.setId(1l);
        segA.setUserName("Augusto");

        User segB = new User();
        segB.setId(2l);
        segB.setUserName("Daniel");

        User segC = new User();
        segC.setId(3l);
        segC.setUserName("Carlos");

        Follower relA = new Follower();
        relA.setSellerId(segA);

        Follower relB = new Follower();
        relB.setSellerId(segB);

        Follower relC = new Follower();
        relC.setSellerId(segC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        when(userRepository.findById(compradorId)).thenReturn(Optional.of(comprador));
        when(followerRepository.findByBuyerId(comprador)).thenReturn(relacoes);
        when(userRepository.findById(relacoes.get(0).getSellerId().getId())).thenReturn(Optional.of(relacoes.get(0).getSellerId()));
        when(userRepository.findById(relacoes.get(1).getSellerId().getId())).thenReturn(Optional.of(relacoes.get(1).getSellerId()));
        when(userRepository.findById(relacoes.get(2).getSellerId().getId())).thenReturn(Optional.of(relacoes.get(2).getSellerId()));


        // ACT
        UserFollowerListDTO dto = userService.listFollowed(compradorId, "name_asc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguindo());

        List<UserDTO> seguidores = dto.getSeguindo();

        assertEquals(3, seguidores.size());
        assertEquals("Augusto", seguidores.get(0).getUser_name());
        assertEquals("Carlos", seguidores.get(1).getUser_name());
        assertEquals("Daniel", seguidores.get(2).getUser_name());
    }

    @Test
    void testListaSeguindoDeveRetornarOrdenadoPorNomeDesc() {
        // ARRANGE
        long compradorId = 5;

        User comprador = new User();
        comprador.setId(compradorId);

        User segA = new User();
        segA.setId(1l);
        segA.setUserName("Augusto");

        User segB = new User();
        segB.setId(2l);
        segB.setUserName("Daniel");

        User segC = new User();
        segC.setId(3l);
        segC.setUserName("Carlos");

        Follower relA = new Follower();
        relA.setSellerId(segA);

        Follower relB = new Follower();
        relB.setSellerId(segB);

        Follower relC = new Follower();
        relC.setSellerId(segC);

        List<Follower> relacoes = List.of(relA, relB, relC);

        when(userRepository.findById(compradorId)).thenReturn(Optional.of(comprador));
        when(followerRepository.findByBuyerId(comprador)).thenReturn(relacoes);
        when(userRepository.findById(relacoes.get(0).getSellerId().getId())).thenReturn(Optional.of(relacoes.get(0).getSellerId()));
        when(userRepository.findById(relacoes.get(1).getSellerId().getId())).thenReturn(Optional.of(relacoes.get(1).getSellerId()));
        when(userRepository.findById(relacoes.get(2).getSellerId().getId())).thenReturn(Optional.of(relacoes.get(2).getSellerId()));

        // ACT
        UserFollowerListDTO dto = userService.listFollowed(compradorId, "name_desc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguindo());

        List<UserDTO> seguidores = dto.getSeguindo();

        assertEquals(3, seguidores.size());
        assertEquals("Daniel", seguidores.get(0).getUser_name());
        assertEquals("Carlos", seguidores.get(1).getUser_name());
        assertEquals("Augusto", seguidores.get(2).getUser_name());
    }

    @Test
    void testListaSeguindoUsuarioNaoCadastro(){

        // arrange
        long compradorId = 14;

        User vendedor = new User();

        vendedor.setId(compradorId);
        when(userRepository.findById(compradorId)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.listFollowers(compradorId, "name_desc")
        );

        // ASSERT
        assertEquals("O usuario não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testListaSeguindoExcecaoUsuarioInvalido(){

        // arrange
        long vendedorId = 0;

        User vendedor = new User();

        vendedor.setId(vendedorId);

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.listFollowers(vendedorId, "name_desc")
        );

        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testDeixarSeguirCorreto(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        User comprador = new User();
        comprador.setId(compradorId);

        User vendedor = new User();
        vendedor.setId(vendedorId);

        when(userRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(userRepository.findById(compradorId)).thenReturn(Optional.of(comprador));

        // ACT
        userService.unfollow(compradorId, vendedorId);

        // ASSERT

        ArgumentCaptor<User> compradorCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<User> vendedorCaptor = ArgumentCaptor.forClass(User.class);


        verify(followerRepository).deleteByBuyerIdAndSellerId(
                compradorCaptor.capture(),
                vendedorCaptor.capture()
        );

        User compradorEnviado = compradorCaptor.getValue();
        User vendedorEnviado = vendedorCaptor.getValue();

        assertEquals(comprador.getId(), compradorEnviado.getId());
        assertEquals(vendedor.getId(), vendedorEnviado.getId());

        verify(followerRepository).deleteByBuyerIdAndSellerId(comprador, vendedor);

    }

    @Test
    void testDeixarSeguirExcecaoVendedorNaoCadastra(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        User comprador = new User();
        comprador.setId(compradorId);

        User vendedor = new User();
        vendedor.setId(vendedorId);

        when(userRepository.findById(vendedorId)).thenReturn(Optional.empty());
        when(userRepository.findById(compradorId)).thenReturn(Optional.of(comprador));

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.unfollow(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O usuario (vendedor) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testDeixarSeguirExcecaoCompradorNaoCadastra(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        User comprador = new User();
        comprador.setId(compradorId);

        when(userRepository.findById(compradorId)).thenReturn(Optional.empty());

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.unfollow(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O usuario (comprador) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testDeixarSeguirExcecaoUsuarioInvalido(){
        // ARRANGE
        long compradorId = 0;
        long vendedorId = 10;

        User comprador = new User();
        comprador.setId(compradorId);

        User vendedor = new User();
        vendedor.setId(vendedorId);

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.unfollow(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }






}