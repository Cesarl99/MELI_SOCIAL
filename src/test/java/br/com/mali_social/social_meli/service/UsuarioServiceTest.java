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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private SeguidoresRepository seguidoresRepository;

    @Mock
    private Verificacao verificacao;

    @Test
    void TestSalvaUsuarioSucesso() {
        // ARRANGE
        UsuarioDto usuariodtoTest = new UsuarioDto();
        usuariodtoTest.setUser_name("Luis");

        // ACT
        usuarioService.salvarUsuario(usuariodtoTest);

        // ASSERT
        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(usuarioRepository, times(1)).save(captor.capture());

        UsuarioEntity entidadeQueFoiSalva = captor.getValue();
        assertEquals("Luis", entidadeQueFoiSalva.getNome());
    }

    @Test
    void TestSeguirComSucesso() {
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        SeguidoresEntity salvo = new SeguidoresEntity();
        salvo.setCompradorId(comprador);
        salvo.setVendedorId(vendedor);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.of(comprador));
        when(seguidoresRepository.save(any(SeguidoresEntity.class))).thenReturn(salvo);

        // ACT
        usuarioService.seguir(compradorId, vendedorId);

        // ASSERT
        ArgumentCaptor<SeguidoresEntity> segCaptor = ArgumentCaptor.forClass(SeguidoresEntity.class);
        verify(seguidoresRepository).save(segCaptor.capture());
        SeguidoresEntity enviadoParaSalvar = segCaptor.getValue();

        assertEquals(comprador, enviadoParaSalvar.getCompradorId());
        assertEquals(vendedor, enviadoParaSalvar.getVendedorId());
    }

    @Test
    void TestSeguirExececaoCompradorNaoCadastrado(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.empty());

        // ACT
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.seguir(compradorId, vendedorId)
        );
        // ASSERT
        assertEquals("O usuario (comprador) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void TestSeguirExcecaoVendedorNaoCadastro(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);
        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.empty());

        // ACT
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.seguir(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O usuario (vendedor) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void TestSeguirExcecaoIdUsuarioInvalido(){
        // ARRANGE
        long compradorId = 0;
        long vendedorId = 0;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        // ACT
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.seguir(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

    }

    @Test
    void TestContarSeguidoresCorreto(){
        // arrange
        long vendedorId = 14;

        UsuarioEntity vendedor = new UsuarioEntity();

        vendedor.setId(vendedorId);
        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(seguidoresRepository.countByVendedorId(vendedor)).thenReturn(25);

        // act
        QuatidadeSeguidoresDto seguidores = usuarioService.contaSeguidores(vendedorId);

        assertNotNull(seguidores);

        assertEquals(25, seguidores.getFollowersCount());
    }

    @Test
    void TestContarSeguidoresExcecaoUsuarioNaoCadastrado(){

        // arrange
        long vendedorId = 14;

        UsuarioEntity vendedor = new UsuarioEntity();

        vendedor.setId(vendedorId);
        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.empty());


        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.contaSeguidores(vendedorId)
        );


        // ASSERT
        assertEquals("O usuario não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void TestContarSeguidoresExcecaoIdInvalido(){

        // arrange
        long vendedorId = 0;

        UsuarioEntity vendedor = new UsuarioEntity();

        vendedor.setId(vendedorId);


        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.contaSeguidores(vendedorId)
        );


        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void listaSeguidoresDeveRetornarOrdenadoPorNomeAsc() {
        // ARRANGE
        long vendedorId = 5;

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        UsuarioEntity segA = new UsuarioEntity();
        segA.setId(1l);
        segA.setNome("Ana");

        UsuarioEntity segB = new UsuarioEntity();
        segB.setId(2l);
        segB.setNome("Carlos");

        UsuarioEntity segC = new UsuarioEntity();
        segC.setId(3l);
        segC.setNome("Bruno");

        SeguidoresEntity relA = new SeguidoresEntity();
        relA.setCompradorId(segA);

        SeguidoresEntity relB = new SeguidoresEntity();
        relB.setCompradorId(segB);

        SeguidoresEntity relC = new SeguidoresEntity();
        relC.setCompradorId(segC);

        List<SeguidoresEntity> relacoes = List.of(relA, relB, relC);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(seguidoresRepository.findByVendedorId(vendedor)).thenReturn(relacoes);
        when(usuarioRepository.findById(relacoes.get(0).getCompradorId().getId())).thenReturn(Optional.of(relacoes.get(0).getCompradorId()));
        when(usuarioRepository.findById(relacoes.get(1).getCompradorId().getId())).thenReturn(Optional.of(relacoes.get(1).getCompradorId()));
        when(usuarioRepository.findById(relacoes.get(2).getCompradorId().getId())).thenReturn(Optional.of(relacoes.get(2).getCompradorId()));


        // ACT
        ListaUsuariosSeguindoresDto dto = usuarioService.listaSeguidores(vendedorId, "name_asc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguidores());

        List<UsuarioDto> seguidores = dto.getSeguidores();

        assertEquals(3, seguidores.size());
        assertEquals("Ana", seguidores.get(0).getUser_name());
        assertEquals("Bruno", seguidores.get(1).getUser_name());
        assertEquals("Carlos", seguidores.get(2).getUser_name());
    }

    @Test
    void listaSeguidoresDeveRetornarOrdenadoPorNomeDesc() {
        // ARRANGE
        long vendedorId = 5;

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        UsuarioEntity segA = new UsuarioEntity();
        segA.setId(1l);
        segA.setNome("Ana");

        UsuarioEntity segB = new UsuarioEntity();
        segB.setId(2l);
        segB.setNome("Carlos");

        UsuarioEntity segC = new UsuarioEntity();
        segC.setId(3l);
        segC.setNome("Bruno");

        SeguidoresEntity relA = new SeguidoresEntity();
        relA.setCompradorId(segA);

        SeguidoresEntity relB = new SeguidoresEntity();
        relB.setCompradorId(segB);

        SeguidoresEntity relC = new SeguidoresEntity();
        relC.setCompradorId(segC);

        List<SeguidoresEntity> relacoes = List.of(relA, relB, relC);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(seguidoresRepository.findByVendedorId(vendedor)).thenReturn(relacoes);
        when(usuarioRepository.findById(relacoes.get(0).getCompradorId().getId())).thenReturn(Optional.of(relacoes.get(0).getCompradorId()));
        when(usuarioRepository.findById(relacoes.get(1).getCompradorId().getId())).thenReturn(Optional.of(relacoes.get(1).getCompradorId()));
        when(usuarioRepository.findById(relacoes.get(2).getCompradorId().getId())).thenReturn(Optional.of(relacoes.get(2).getCompradorId()));


        // ACT
        ListaUsuariosSeguindoresDto dto = usuarioService.listaSeguidores(vendedorId, "name_desc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguidores());

        List<UsuarioDto> seguidores = dto.getSeguidores();

        assertEquals(3, seguidores.size());
        assertEquals("Carlos", seguidores.get(0).getUser_name());
        assertEquals("Bruno", seguidores.get(1).getUser_name());
        assertEquals("Ana", seguidores.get(2).getUser_name());
    }

    @Test
    void listaSeguidoresUsuarioNaoCadastro(){

        // arrange
        long vendedorId = 14;

        UsuarioEntity vendedor = new UsuarioEntity();

        vendedor.setId(vendedorId);
        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.listaSeguidores(vendedorId, "name_desc")
        );

        // ASSERT
        assertEquals("O usuario não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void listaSeguidoresExcecaoUsuarioInvalido(){

        // arrange
        long vendedorId = 0;

        UsuarioEntity vendedor = new UsuarioEntity();

        vendedor.setId(vendedorId);

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.listaSeguidores(vendedorId, "name_desc")
        );


        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void listaSeguindoDeveRetornarOrdenadoPorNomeAsc() {
        // ARRANGE
        long compradorId = 5;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity segA = new UsuarioEntity();
        segA.setId(1l);
        segA.setNome("Augusto");

        UsuarioEntity segB = new UsuarioEntity();
        segB.setId(2l);
        segB.setNome("Daniel");

        UsuarioEntity segC = new UsuarioEntity();
        segC.setId(3l);
        segC.setNome("Carlos");

        SeguidoresEntity relA = new SeguidoresEntity();
        relA.setVendedorId(segA);

        SeguidoresEntity relB = new SeguidoresEntity();
        relB.setVendedorId(segB);

        SeguidoresEntity relC = new SeguidoresEntity();
        relC.setVendedorId(segC);

        List<SeguidoresEntity> relacoes = List.of(relA, relB, relC);

        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.of(comprador));
        when(seguidoresRepository.findByCompradorId(comprador)).thenReturn(relacoes);
        when(usuarioRepository.findById(relacoes.get(0).getVendedorId().getId())).thenReturn(Optional.of(relacoes.get(0).getVendedorId()));
        when(usuarioRepository.findById(relacoes.get(1).getVendedorId().getId())).thenReturn(Optional.of(relacoes.get(1).getVendedorId()));
        when(usuarioRepository.findById(relacoes.get(2).getVendedorId().getId())).thenReturn(Optional.of(relacoes.get(2).getVendedorId()));


        // ACT
        ListaUsuarioSeguindoDto dto = usuarioService.listaSeguindo(compradorId, "name_asc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguindo());

        List<UsuarioDto> seguidores = dto.getSeguindo();

        assertEquals(3, seguidores.size());
        assertEquals("Augusto", seguidores.get(0).getUser_name());
        assertEquals("Carlos", seguidores.get(1).getUser_name());
        assertEquals("Daniel", seguidores.get(2).getUser_name());
    }

    @Test
    void listaSeguindoDeveRetornarOrdenadoPorNomeDesc() {
        // ARRANGE
        long compradorId = 5;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity segA = new UsuarioEntity();
        segA.setId(1l);
        segA.setNome("Augusto");

        UsuarioEntity segB = new UsuarioEntity();
        segB.setId(2l);
        segB.setNome("Daniel");

        UsuarioEntity segC = new UsuarioEntity();
        segC.setId(3l);
        segC.setNome("Carlos");

        SeguidoresEntity relA = new SeguidoresEntity();
        relA.setVendedorId(segA);

        SeguidoresEntity relB = new SeguidoresEntity();
        relB.setVendedorId(segB);

        SeguidoresEntity relC = new SeguidoresEntity();
        relC.setVendedorId(segC);

        List<SeguidoresEntity> relacoes = List.of(relA, relB, relC);

        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.of(comprador));
        when(seguidoresRepository.findByCompradorId(comprador)).thenReturn(relacoes);
        when(usuarioRepository.findById(relacoes.get(0).getVendedorId().getId())).thenReturn(Optional.of(relacoes.get(0).getVendedorId()));
        when(usuarioRepository.findById(relacoes.get(1).getVendedorId().getId())).thenReturn(Optional.of(relacoes.get(1).getVendedorId()));
        when(usuarioRepository.findById(relacoes.get(2).getVendedorId().getId())).thenReturn(Optional.of(relacoes.get(2).getVendedorId()));


        // ACT
        ListaUsuarioSeguindoDto dto = usuarioService.listaSeguindo(compradorId, "name_desc");

        // ASSERT
        assertNotNull(dto);
        assertNotNull(dto.getSeguindo());

        List<UsuarioDto> seguidores = dto.getSeguindo();

        assertEquals(3, seguidores.size());
        assertEquals("Daniel", seguidores.get(0).getUser_name());
        assertEquals("Carlos", seguidores.get(1).getUser_name());
        assertEquals("Augusto", seguidores.get(2).getUser_name());
    }

    @Test
    void listaSeguindoUsuarioNaoCadastro(){

        // arrange
        long compradorId = 14;

        UsuarioEntity vendedor = new UsuarioEntity();

        vendedor.setId(compradorId);
        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.empty());

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.listaSeguindo(compradorId, "name_desc")
        );

        // ASSERT
        assertEquals("O usuario não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void listaSeguindoExcecaoUsuarioInvalido(){

        // arrange
        long vendedorId = 0;

        UsuarioEntity vendedor = new UsuarioEntity();

        vendedor.setId(vendedorId);

        // act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.listaSeguindo(vendedorId, "name_desc")
        );

        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void DeixarSeguirCorreto(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.of(vendedor));
        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.of(comprador));

        // ACT
        usuarioService.deixarDeSeguir(compradorId, vendedorId);

        // ASSERT

        ArgumentCaptor<UsuarioEntity> compradorCaptor = ArgumentCaptor.forClass(UsuarioEntity.class);
        ArgumentCaptor<UsuarioEntity> vendedorCaptor = ArgumentCaptor.forClass(UsuarioEntity.class);


        verify(seguidoresRepository).deleteByCompradorIdAndVendedorId(
                compradorCaptor.capture(),
                vendedorCaptor.capture()
        );

        UsuarioEntity compradorEnviado = compradorCaptor.getValue();
        UsuarioEntity vendedorEnviado = vendedorCaptor.getValue();

        assertEquals(comprador.getId(), compradorEnviado.getId());
        assertEquals(vendedor.getId(), vendedorEnviado.getId());

        verify(seguidoresRepository).deleteByCompradorIdAndVendedorId(comprador, vendedor);

    }

    @Test
    void DeixarSeguirExcecaoVendedorNaoCadastra(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        when(usuarioRepository.findById(vendedorId)).thenReturn(Optional.empty());
        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.of(comprador));

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.deixarDeSeguir(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O usuario (vendedor) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void DeixarSeguirExcecaoCompradorNaoCadastra(){
        // ARRANGE
        long compradorId = 10;
        long vendedorId = 20;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        when(usuarioRepository.findById(compradorId)).thenReturn(Optional.empty());

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.deixarDeSeguir(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O usuario (comprador) não encontrado.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void DeixarSeguirExcecaoUsuarioInvalido(){
        // ARRANGE
        long compradorId = 0;
        long vendedorId = 10;

        UsuarioEntity comprador = new UsuarioEntity();
        comprador.setId(compradorId);

        UsuarioEntity vendedor = new UsuarioEntity();
        vendedor.setId(vendedorId);

        // ACT

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.deixarDeSeguir(compradorId, vendedorId)
        );

        // ASSERT
        assertEquals("O id não pode estar vazio. id deve ser maior que zero.", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }






}