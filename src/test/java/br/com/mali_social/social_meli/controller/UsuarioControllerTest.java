package br.com.mali_social.social_meli.controller;

import br.com.mali_social.social_meli.dto.seguidor.ListaUsuarioSeguindoDto;
import br.com.mali_social.social_meli.dto.seguidor.ListaUsuariosSeguindoresDto;
import br.com.mali_social.social_meli.dto.seguidor.QuatidadeSeguidoresDto;
import br.com.mali_social.social_meli.dto.usuario.UsuarioDto;
import br.com.mali_social.social_meli.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void salvarUsuarioComSucesso() throws Exception {
        UsuarioDto dto = new UsuarioDto();
        dto.setUser_name("Luis");

        doNothing().when(usuarioService).salvarUsuario(any(UsuarioDto.class));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Mockito.verify(usuarioService).salvarUsuario(any(UsuarioDto.class));
    }

    @Test
    void buscarUsuarioPorIdComSucesso() throws Exception {
        long userId = 10L;
        UsuarioDto dto = new UsuarioDto();
        dto.setUser_id(userId);
        dto.setUser_name("João Teste");

        when(usuarioService.pesquisaUsuarios(userId)).thenReturn(dto);

        mockMvc.perform(get("/users/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(10))
                .andExpect(jsonPath("$.user_name").value("João Teste"));
    }

    @Test
    void SeguirUsuarioComSucesso() throws Exception {
        int userId = 1;
        int userIdToFollow = 2;

        doNothing().when(usuarioService).seguir(userId, userIdToFollow);

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", userId, userIdToFollow))
                .andExpect(status().isOk());

        Mockito.verify(usuarioService).seguir(userId, userIdToFollow);
    }

    @Test
    void retornarQuantidadeSeguidoresComSucesso() throws Exception {
        long userId = 1L;
        QuatidadeSeguidoresDto dto = new QuatidadeSeguidoresDto(userId, "João", 3);

        when(usuarioService.contaSeguidores(userId)).thenReturn(dto);

        mockMvc.perform(get("/users/{userId}/followers/count", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userName").value("João"))
                .andExpect(jsonPath("$.followersCount").value(3));
    }

    @Test
    void listarSeguidoresComOrdenacaoPadraoComSucesso() throws Exception {
        int userId = 1;

        UsuarioDto seg1 = new UsuarioDto(10L, "Carlos");
        UsuarioDto seg2 = new UsuarioDto(9L, "Kim");

        ListaUsuariosSeguindoresDto dto =
                new ListaUsuariosSeguindoresDto(userId, "João Teste", List.of(seg1, seg2));

        when(usuarioService.listaSeguidores(eq(userId), eq("name_asc")))
                .thenReturn(dto);

        mockMvc.perform(get("/users/{userId}/followers/list", userId)
                        .param("order", "name_asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void listarQuemEstouSeguindoComOrdenacaoDescComSucesso() throws Exception {
        int userId = 1;

        UsuarioDto seg1 = new UsuarioDto(10L, "Carlos");
        UsuarioDto seg2 = new UsuarioDto(9L, "Kim");

        ListaUsuarioSeguindoDto dto =
                new ListaUsuarioSeguindoDto(userId, "João Teste", List.of(seg1, seg2));

        when(usuarioService.listaSeguindo(eq(userId), eq("name_desc")))
                .thenReturn(dto);

        mockMvc.perform(get("/users/{userId}/followed/list", userId)
                        .param("order", "name_desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deixarDeSeguirUsuarioComSucesso() throws Exception {
        int userId = 1;
        int userIdToUnfollow = 2;

        doNothing().when(usuarioService).deixarDeSeguir(userId, userIdToUnfollow);

        mockMvc.perform(post("/users/{userId}/unfollow/{userIdToUnfollow}", userId, userIdToUnfollow))
                .andExpect(status().isOk());

        Mockito.verify(usuarioService).deixarDeSeguir(userId, userIdToUnfollow);
    }
}