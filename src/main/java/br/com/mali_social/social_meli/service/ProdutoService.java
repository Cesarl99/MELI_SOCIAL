package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.produto.ProdutoDto;
import br.com.mali_social.social_meli.entity.ProdutosEntity;
import br.com.mali_social.social_meli.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService (ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public ProdutosEntity salvarProduto(ProdutoDto produtoDto){

        ProdutosEntity produto = new ProdutosEntity();

        produto.setId(produtoDto.getProduct_id());
        produto.setNome_produto(produtoDto.getProduct_name());
        produto.setTipo(produtoDto.getType());
        produto.setMarca(produtoDto.getBrand());
        produto.setCor(produtoDto.getColor());
        produto.setNotas(produtoDto.getNotes());

        return produtoRepository.save(produto);
    }

}
