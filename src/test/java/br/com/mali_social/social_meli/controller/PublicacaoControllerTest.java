package br.com.mali_social.social_meli.controller;

import br.com.mali_social.social_meli.dto.publicacao.ListaPublicacaoDescontoUsuarioDto;
import br.com.mali_social.social_meli.dto.publicacao.QuantidadePublicacaoDescontoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.mali_social.social_meli.dto.publicacao.ListaPublicacaoUsuariosDto;
import br.com.mali_social.social_meli.dto.publicacao.PublicacaoDto;
import br.com.mali_social.social_meli.dto.produto.ProdutoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class PublicacaoControllerTeste {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void criarPublicacaoComSucesso() throws Exception {

        ProdutoDto produto = new ProdutoDto();
        produto.setProduct_id(null);
        produto.setProduct_name("Headset RGB Inalámbrico");
        produto.setType("Gamer");
        produto.setBrand("Razer");
        produto.setColor("Green with RGB");
        produto.setNotes("Sem bateria");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String data = LocalDate.of(2026, 1, 10).format(formatter);

        PublicacaoDto publicacaoDto = new PublicacaoDto();
        publicacaoDto.setUser_id(1L);
        publicacaoDto.setPost_id(null);
        publicacaoDto.setDate(data);
        publicacaoDto.setProduct(produto);
        publicacaoDto.setCategory(120);
        publicacaoDto.setPrice(2800.69);
        publicacaoDto.setHas_promo(false);
        publicacaoDto.setDiscount(0.0);

        String jsonBody = objectMapper.writeValueAsString(publicacaoDto);

        mockMvc.perform(post("/products/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    void listarPublicacoesDosSeguidosEmOrdemAsc() throws Exception {
        Long userId = 1L;
        String order = "date_asc";

        var mvcResult = mockMvc.perform(get("/products/followed/{userId}/list", userId)
                        .param("order", order)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();

        ListaPublicacaoUsuariosDto resposta =
                objectMapper.readValue(responseJson, ListaPublicacaoUsuariosDto.class);

        assertThat(resposta).isNotNull();
        assertThat(resposta.getUser_id()).isEqualTo(userId);

        assertThat(resposta.getPublicacoes()).isNotNull();


        if (!resposta.getPublicacoes().isEmpty()) {

            PublicacaoDto primeira = resposta.getPublicacoes().get(0);
            PublicacaoDto ultima = resposta.getPublicacoes()
                    .get(resposta.getPublicacoes().size() - 1);

            LocalDate d1 = LocalDate.parse(primeira.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            LocalDate d2 = LocalDate.parse(ultima.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            assertThat(d1).isBeforeOrEqualTo(d2);
        }
    }

    @Test
    void criarPublicacaoComDesconto() throws Exception {

        ProdutoDto produto = new ProdutoDto();
        produto.setProduct_name("Cadeira Gamer");
        produto.setType("Gamer");
        produto.setBrand("Racer");
        produto.setColor("Red & Black");
        produto.setNotes("Special Edition");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String data = LocalDate.of(2026, 1, 10).format(formatter);

        PublicacaoDto publicacao = new PublicacaoDto();
        publicacao.setUser_id(1L);
        publicacao.setDate(data);
        publicacao.setProduct(produto);
        publicacao.setCategory(100);
        publicacao.setPrice(15000.50);
        publicacao.setHas_promo(true);
        publicacao.setDiscount(10.0); // 10%

        String bodyJson = objectMapper.writeValueAsString(publicacao);

        mockMvc.perform(post("/products/promo-pub")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyJson))
                .andExpect(status().isOk()); // seu método retorna void → 200 OK
    }


    @Test
    void contarPublicacoesComDescontoDeUmUsuario() throws Exception {
        Long userId = 1L;

        var mvcResult = mockMvc.perform(
                        get("/products/promo-pub/count")
                                .param("user_id", String.valueOf(userId))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();

        QuantidadePublicacaoDescontoDto resposta = objectMapper.readValue(json, QuantidadePublicacaoDescontoDto.class);

        assertThat(resposta).isNotNull();
        assertThat(resposta.getUser_id()).isEqualTo(userId);
        assertThat(resposta.getPromo_products_count()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void listarPublicacoesComDescontoDeUmUsuario() throws Exception {
        Long userId = 1L;

        var mvcResult = mockMvc.perform(
                        get("/products/promo-pub/list")
                                .param("user_id", String.valueOf(userId))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();

        ListaPublicacaoDescontoUsuarioDto resposta =
                objectMapper.readValue(json, ListaPublicacaoDescontoUsuarioDto.class);

        assertThat(resposta).isNotNull();
         assertThat(resposta.getUser_id()).isEqualTo(userId);
         assertThat(resposta.getPosts()).isNotNull();


         if (!resposta.getPosts().isEmpty()) {
             PublicacaoDto primeira = resposta.getPosts().get(0);
             assertThat(primeira.isHas_promo()).isTrue();
         }
    }
}