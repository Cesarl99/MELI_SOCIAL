package br.com.mali_social.social_meli.service;


import br.com.mali_social.social_meli.dto.produto.ProdutoDto;
import br.com.mali_social.social_meli.dto.publicacao.PublicacaoDto;
import br.com.mali_social.social_meli.entity.ProdutosEntity;
import br.com.mali_social.social_meli.entity.PublicacaoEntity;
import br.com.mali_social.social_meli.entity.SeguidoresEntity;
import br.com.mali_social.social_meli.entity.UsuarioEntity;
import br.com.mali_social.social_meli.repository.ProdutoRepository;
import br.com.mali_social.social_meli.repository.PublicacaoRepository;
import br.com.mali_social.social_meli.repository.UsuarioRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class PublicacaoServiceTest {

    @InjectMocks
    private PublicacaoService publicacaoService;

    @InjectMocks
    private ProdutosEntity produtosEntity;

    @Mock
    private PublicacaoRepository publicacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    void TestSalvarPublicacaoCorreto(){
        long vendedorId = 10L;
        // ARRANGE
        ProdutoDto produto = new ProdutoDto(
                "Edição Especial", "Verde", "Brasil", "Cadeira", "Cadeira Gamer", 14l
        );

        PublicacaoDto publicacao = new PublicacaoDto(
            0l, vendedorId,"08-01-2026", produto, 16, 190.00, false, 0
        );

        ProdutosEntity produtoEnt = new ProdutosEntity(
                produto.getProduct_name(), produto.getColor(), produto.getBrand(), produto.getColor(), produto.getNotes()
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



}
