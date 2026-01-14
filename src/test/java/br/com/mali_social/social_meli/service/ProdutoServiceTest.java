package br.com.mali_social.social_meli.service;


import br.com.mali_social.social_meli.dto.produto.ProdutoDto;
import br.com.mali_social.social_meli.entity.ProdutosEntity;
import br.com.mali_social.social_meli.entity.PublicacaoEntity;
import br.com.mali_social.social_meli.entity.UsuarioEntity;
import br.com.mali_social.social_meli.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

public class ProdutoServiceTest {


    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Test
    void TestSalvarProdutoCorreto(){
        // ARRANGE
        ProdutoDto produto = new ProdutoDto(
                "Cadeira Gamer", "Edição especial", "Verde", 14l, "Brasil", "Cadeira");


        ProdutosEntity produtoEnt = new ProdutosEntity();
        when(produtoRepository.save(any(ProdutosEntity.class))).thenReturn(produtoEnt);

        // ACT
        produtoService.salvarProduto(produto);

        // ASSERT
        ArgumentCaptor<ProdutosEntity> captor = ArgumentCaptor.forClass(ProdutosEntity.class);
        verify(produtoRepository, times(1)).save(captor.capture());

        ProdutosEntity ProdutoSalvo = captor.getValue();
        assertEquals("Edição especial", ProdutoSalvo.getNotas());
        assertEquals("Verde", ProdutoSalvo.getCor());
        assertEquals("Brasil", ProdutoSalvo.getMarca());
        assertEquals("Cadeira", ProdutoSalvo.getTipo());
        assertEquals("Cadeira Gamer", ProdutoSalvo.getNome_produto());

    }

}
