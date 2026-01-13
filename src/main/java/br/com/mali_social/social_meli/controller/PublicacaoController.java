package br.com.mali_social.social_meli.controller;

import br.com.mali_social.social_meli.dto.publicacao.ListaPublicacaoDescontoUsuarioDto;
import br.com.mali_social.social_meli.dto.publicacao.ListaPublicacaoUsuariosDto;
import br.com.mali_social.social_meli.dto.publicacao.PublicacaoDto;
import br.com.mali_social.social_meli.dto.publicacao.QuantidadePublicacaoDescontoDto;
import br.com.mali_social.social_meli.entity.ProdutosEntity;
import br.com.mali_social.social_meli.service.ProdutoService;
import br.com.mali_social.social_meli.service.PublicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("products")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;
    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/publish")
    public void criaPublicacao(@RequestBody PublicacaoDto publicacaoDto){
        System.out.println("recebendo a publicacao");
        ProdutosEntity produto = produtoService.salvarProduto(publicacaoDto.getProduct());
        publicacaoService.salvarPublicacao(publicacaoDto, produto);
    }

    @GetMapping("/followed/{userId}/list")
    public ListaPublicacaoUsuariosDto listaPublicacaoUsuario(@PathVariable Long userId,
                                                             @RequestParam(name = "order", required = false, defaultValue = "date_asc") String order){
        return publicacaoService.listaPublicacaoUsuario(userId, order);
    }

    @PostMapping ("/promo-pub")
    public void criaPublicacaoDesconto(@RequestBody PublicacaoDto publicacaoDto){

        System.out.println("publicacao recebida" + publicacaoDto);
        ProdutosEntity produto = produtoService.salvarProduto(publicacaoDto.getProduct());
        String ret = publicacaoService.salvarPublicacao(publicacaoDto, produto);
        System.out.println("valor do ret" + ret);
    }

    @GetMapping ("/promo-pub/count")
    public QuantidadePublicacaoDescontoDto contaPublicacaoDesconto (@RequestParam (name = "user_id", required = true) Long user_id){
        return publicacaoService.contaPublicacaoDesconto(user_id);
    }

    @GetMapping ("/promo-pub/list")
    public ListaPublicacaoDescontoUsuarioDto listaPromocaoUsuario (@RequestParam(name = "user_id", required = true) Long userId){
        return publicacaoService.listaPublicacaoUsuariosDesconto(userId);
    }

}
