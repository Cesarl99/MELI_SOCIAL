package br.com.mali_social.social_meli.controller;


import br.com.mali_social.social_meli.dto.ListaUsuariosSeguindoresDto;
import br.com.mali_social.social_meli.dto.QuatidadeSeguidoresDto;
import br.com.mali_social.social_meli.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public String SeguirUsuario(@PathVariable long userId, @PathVariable long userIdToFollow){
        System.out.println("o usuario do id " + userId + "está seguindo o usuario do id " + userIdToFollow);
        return usuarioService.seguir(userId, userIdToFollow);
    }

    @GetMapping("/{userId}/followers/count")
    public QuatidadeSeguidoresDto ContaSeguidores (@PathVariable long userId){
        System.out.println("contagem de usuarios :"+userId);
        return usuarioService.contaSeguidores(userId);
    }

    @GetMapping("/{userId}/followers/list")
    public ListaUsuariosSeguindoresDto ListaSeguidores (
            @PathVariable int userId){
        return usuarioService.listaSeguidores(userId);
    }
//
//    @GetMapping("/{userId}/followed/list")
//    public String ListaSeguindo (
//        @PathVariable int userId,
//        @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order
//    ){
//        Sort sort = mapOrderToSort(order);
//        System.out.println("contagem de usuarios :"+userId);
//        return usuarioService.listaSeguindo(userId, sort);
//    }

    @GetMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public String deixarSeguir (@PathVariable int userId, @PathVariable int userIdToUnfollow){
        return ("o usuario com o id "+ userId +" deixou de seguir o usuario com o id " + userIdToUnfollow);
    }

//    private Sort mapOrderToSort(String order) {
//        return switch (order) {
//            case "name_asc"  -> Sort.by("UserName").ascending();
//            case "name_desc" -> Sort.by("UserName").descending();
//            default -> throw new IllegalArgumentException("Parâmetro 'order' inválido: " + order);
//        };
//    }

}


