package br.com.mali_social.social_meli.controller;

import br.com.mali_social.social_meli.dto.ListaPublicacaoUsuariosDto;
import br.com.mali_social.social_meli.dto.PublicacaoDto;
import br.com.mali_social.social_meli.entity.ProdutosEntity;
import br.com.mali_social.social_meli.service.ProdutoService;
import br.com.mali_social.social_meli.service.PublicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("products")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;
    @Autowired
    private ProdutoService produtoService;
    @PostMapping("/publish")
    public String criarPublicacao(@RequestBody PublicacaoDto publicacaoDto){
        // TODO ADICIONAR AS VERIFICAÇÕES
        ProdutosEntity produto = produtoService.salvarProduto(publicacaoDto.getProduct());
        return publicacaoService.salvarPublicacao(publicacaoDto, produto);
    }

    @GetMapping("/followed/{userId}/list")
    public ListaPublicacaoUsuariosDto listaPublicacaoUsuario(@PathVariable Long userId){
        return publicacaoService.ListaPublicacaoUsuario(userId);
    }

    @PostMapping ("/promo-pub")
    public String criaPublicacaoDesconto(@PathVariable PublicacaoDto publicacaoDto){
        return publicacaoService.salvarPublicacao(publicacaoDto, null);
    }

    @GetMapping ("/promo-pub/list")
    public String listaPromocaoUsuario (@RequestParam(name = "user_id") int userId){
        return ("LISTANDO PROMOÇÓES DO USUARIO COMO O ID " + userId);
    }


//    private Sort mapOrderToSort(String order) {
//        return switch (order) {
//            case "date_asc"  -> Sort.by("date").ascending();
//            case "date_desc" -> Sort.by("date").descending();
//            default -> throw new IllegalArgumentException("Parâmetro 'order' inválido: " + order);
//        };
//    }


}
