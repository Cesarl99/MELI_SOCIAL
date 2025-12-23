package br.com.mali_social.social_meli.controller;

import br.com.mali_social.social_meli.dto.PublicacaoDto;
import br.com.mali_social.social_meli.service.PublicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("products")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;
    @PostMapping("/publish")
    public String criarPublicacao(@RequestBody PublicacaoDto publicacaoDto){
        System.out.println("categoria: " + publicacaoDto.getCategory() + " data: "+ publicacaoDto.getDate());
        System.out.println("preço" + publicacaoDto.getPrice() + " user_id" + publicacaoDto.getUser_id());
        System.out.println("produto_id " + publicacaoDto.getProduct().getProduct_id());
        System.out.println("produto nome " + publicacaoDto.getProduct().getProduct_name());
        System.out.println("produto tipo " + publicacaoDto.getProduct().getType());
        System.out.println("produto marca " + publicacaoDto.getProduct().getBrand());
        System.out.println("produto Cor " + publicacaoDto.getProduct().getColor());
        System.out.println("produto Notas" + publicacaoDto.getProduct().getNotes());

        return publicacaoService.salvarPublicacao(publicacaoDto);
    }
// TODO IMPLEMENTAR APÓS O BANCO DE DADOS
//    @GetMapping("/followed/{userId}/list")
//    public String listaPublicacaoUsuario(
//            @PathVariable int userId,
//            @RequestParam(name = "order", required = false, defaultValue = "date_asc") String order
//            ){
//        Sort sort = mapOrderToSort(order);
//        return ("IMPLEMENTAR LISTA DAS PUBLICACOES");
//    }


    @PostMapping ("/promo-pub")
    public String criaPublicacaoDesconto(@PathVariable PublicacaoDto publicacaoDto){

        return publicacaoService.salvarPublicacao(publicacaoDto);
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
