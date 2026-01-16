package br.com.mali_social.social_meli.controller;

import br.com.mali_social.social_meli.dto.follower.UserFollowerListDTO;
import br.com.mali_social.social_meli.dto.follower.UserFollowersListDTO;
import br.com.mali_social.social_meli.dto.follower.FollowersQuantityDTO;
import br.com.mali_social.social_meli.dto.user.UserDTO;
import br.com.mali_social.social_meli.service.UserService;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void salvarUsuarioComSucesso() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setUser_name("Luis");

        doNothing().when(userService).salvarUsuario(any(UserDTO.class));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        Mockito.verify(userService).salvarUsuario(any(UserDTO.class));
    }

    @Test
    void buscarUsuarioPorIdComSucesso() throws Exception {
        long userId = 10L;
        UserDTO dto = new UserDTO();
        dto.setUser_id(userId);
        dto.setUser_name("João Teste");

        when(userService.pesquisaUsuarios(userId)).thenReturn(dto);

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

        doNothing().when(userService).seguir(userId, userIdToFollow);

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", userId, userIdToFollow))
                .andExpect(status().isOk());

        Mockito.verify(userService).seguir(userId, userIdToFollow);
    }

    @Test
    void retornarQuantidadeSeguidoresComSucesso() throws Exception {
        long userId = 1L;
        FollowersQuantityDTO dto = new FollowersQuantityDTO(userId, "João", 3);

        when(userService.contaSeguidores(userId)).thenReturn(dto);

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

        UserDTO seg1 = new UserDTO(10L, "Carlos");
        UserDTO seg2 = new UserDTO(9L, "Kim");

        UserFollowersListDTO dto =
                new UserFollowersListDTO(userId, "João Teste", List.of(seg1, seg2));

        when(userService.listaSeguidores(eq(userId), eq("name_asc")))
                .thenReturn(dto);

        mockMvc.perform(get("/users/{userId}/followers/list", userId)
                        .param("order", "name_asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void listarQuemEstouSeguindoComOrdenacaoDescComSucesso() throws Exception {
        int userId = 1;

        UserDTO seg1 = new UserDTO(10L, "Carlos");
        UserDTO seg2 = new UserDTO(9L, "Kim");

        UserFollowerListDTO dto =
                new UserFollowerListDTO(userId, "João Teste", List.of(seg1, seg2));

        when(userService.listaSeguindo(eq(userId), eq("name_desc")))
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

        doNothing().when(userService).deixarDeSeguir(userId, userIdToUnfollow);

        mockMvc.perform(post("/users/{userId}/unfollow/{userIdToUnfollow}", userId, userIdToUnfollow))
                .andExpect(status().isOk());

        Mockito.verify(userService).deixarDeSeguir(userId, userIdToUnfollow);
    }
}