package br.com.mali_social.social_meli.controller;


import br.com.mali_social.social_meli.dto.ListaUsuarioSeguindoDto;
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
        return usuarioService.seguir(userId, userIdToFollow);
    }

    @GetMapping("/{userId}/followers/count")
    public QuatidadeSeguidoresDto ContaSeguidores (@PathVariable long userId){
        return usuarioService.contaSeguidores(userId);
    }

    @GetMapping("/{userId}/followers/list")
    public ListaUsuariosSeguindoresDto ListaSeguidores(@PathVariable int userId,
                                                       @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order){
        return usuarioService.listaSeguidores(userId, order);
    }

    @GetMapping("/{userId}/followed/list")
    public ListaUsuarioSeguindoDto ListaSeguindo (@PathVariable int userId,
                                                  @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order){
        return usuarioService.listaSeguindo(userId, order);
    }

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public String DeixarSeguir (@PathVariable int userId, @PathVariable int userIdToUnfollow){
        return usuarioService.deixarDeSeguir(userId, userIdToUnfollow);
    }



}


