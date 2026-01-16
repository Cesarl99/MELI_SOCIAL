package br.com.mali_social.social_meli.controller;


import br.com.mali_social.social_meli.dto.follower.UserFollowerListDTO;
import br.com.mali_social.social_meli.dto.follower.UserFollowersListDTO;
import br.com.mali_social.social_meli.dto.follower.FollowersQuantityDTO;
import br.com.mali_social.social_meli.dto.user.UserDTO;
import br.com.mali_social.social_meli.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public void saveUser(@RequestBody UserDTO user){
        userService.saveUser(user);
    }

    @GetMapping("{userId}")
    public UserDTO searchUser(@PathVariable long userId) {
        return userService.searchUser(userId);
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public void followUser (@PathVariable int userId, @PathVariable int userIdToFollow){
        userService.follow(userId, userIdToFollow);
    }

    @GetMapping("/{userId}/followers/count")
    public FollowersQuantityDTO countFollowers (@PathVariable long userId){
        return userService.countFollowers(userId);
    }

    @GetMapping("/{userId}/followers/list")
    public UserFollowersListDTO listFollowers (@PathVariable int userId,
                                                 @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order){
        return userService.listFollowers(userId, order);
    }

    @GetMapping("/{userId}/followed/list")
    public UserFollowerListDTO listFollowed (@PathVariable int userId,
                                              @RequestParam(name = "order", required = false, defaultValue = "name_asc") String order){
        return userService.listFollowed(userId, order);
    }

    @PostMapping("/{userId}/unfollow/{userIdToUnfollow}")
    public void unfollow (@PathVariable int userId, @PathVariable int userIdToUnfollow){
        userService.unfollow(userId, userIdToUnfollow);
    }


}


