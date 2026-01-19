package br.com.mali_social.social_meli.service;


import br.com.mali_social.social_meli.dto.product.ProductDTO;
import br.com.mali_social.social_meli.entity.Product;
import br.com.mali_social.social_meli.repository.ProductRepository;
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

public class ProductServiceTest {


    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void testSalvarProdutoCorreto(){
        // ARRANGE
        ProductDTO produto = new ProductDTO(
                "Cadeira Gamer", "Edição especial", "Verde", 14l, "Brasil", "Cadeira");


        Product produtoEnt = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(produtoEnt);

        // ACT
        productService.saveProduct(produto);

        // ASSERT
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(captor.capture());

        Product ProdutoSalvo = captor.getValue();
        assertEquals("Edição especial", ProdutoSalvo.getNotes());
        assertEquals("Verde", ProdutoSalvo.getColor());
        assertEquals("Brasil", ProdutoSalvo.getBrand());
        assertEquals("Cadeira", ProdutoSalvo.getType());
        assertEquals("Cadeira Gamer", ProdutoSalvo.getProduct_name());

    }

}
