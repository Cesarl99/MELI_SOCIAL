package br.com.mali_social.social_meli.controller;


import br.com.mali_social.social_meli.dto.seguidor.ListaUsuarioSeguindoDto;
import br.com.mali_social.social_meli.dto.seguidor.ListaUsuariosSeguindoresDto;
import br.com.mali_social.social_meli.dto.seguidor.QuatidadeSeguidoresDto;
import br.com.mali_social.social_meli.dto.usuario.UsuarioDto;
import br.com.mali_social.social_meli.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public void salvaUsuario(@RequestBody UsuarioDto user){
        System.out.println(user);
        usuarioService.salvarUsuario(user);
    }

    @GetMapping("{userId}")
    public UsuarioDto pesquisaUsuario(@PathVariable long userId) {
        return usuarioService.pesquisaUsuarios(userId);
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public void seguirUsuario (@PathVariable int userId, @PathVariable int userIdToFollow){
        usuarioService.seguir(userId, userIdToFollow);
    }

    @GetMapping("/{userId}/followers/count")
    public QuatidadeSeguidoresDto contaSeguidores (@PathVariable long userId){
        return usuarioService.contaSeguidores(userId);
    }

    @GetMapping("/{userId}/followers/list")
    public ListaUsuariosSeguindoresDto listaSeguidores (@PathVariable int userId,
                                                       @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order){
        return usuarioService.listaSeguidores(userId, order);
    }

    @GetMapping("/{userId}/followed/list")
    public ListaUsuarioSeguindoDto listaSeguindo (@PathVariable int userId,
                                                  @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order){
        return usuarioService.listaSeguindo(userId, order);
    }

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public void deixarSeguir (@PathVariable int userId, @PathVariable int userIdToUnfollow){
        usuarioService.deixarDeSeguir(userId, userIdToUnfollow);
    }


}


