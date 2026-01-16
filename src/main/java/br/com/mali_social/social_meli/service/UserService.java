package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.follower.UserFollowerListDTO;
import br.com.mali_social.social_meli.dto.follower.UserFollowersListDTO;
import br.com.mali_social.social_meli.dto.follower.FollowersQuantityDTO;
import br.com.mali_social.social_meli.dto.user.UserDTO;
import br.com.mali_social.social_meli.entity.Follower;
import br.com.mali_social.social_meli.entity.User;
import br.com.mali_social.social_meli.repository.FollowerRepository;
import br.com.mali_social.social_meli.repository.UserRepository;
import br.com.mali_social.social_meli.util.Verification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class UserService {
    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;
    private final Verification verification = new Verification();

    public UserService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    public void saveUser(UserDTO usuario) {
        User user = new User(usuario.getUser_id(), usuario.getUser_name());
        userRepository.save(user);
    }

    public UserDTO searchUser(long UserId){
        User userEnt = userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (comprador) não encontrado."));
        return new UserDTO(
                userEnt.getId(),
                userEnt.getUserName()
        );
    }

    public void follow(long UserId, long userIdToFollow){
        Follower followers = new Follower();
        verification.checkUserId(UserId);
        verification.checkUserId(userIdToFollow);
        User seller = userRepository.findById(userIdToFollow).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (vendedor) não encontrado."));
        User buyer = userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (comprador) não encontrado."));
        boolean exits = followerRepository.existsByBuyerIdAndSellerId(buyer, seller);
        if (exits) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já segue essse vendedor.");
        }
        followers.setSellerId(seller);
        followers.setBuyerId(buyer);
        followerRepository.save(followers).toString();
    }

    public FollowersQuantityDTO countFollowers(long UserId){
        verification.checkUserId(UserId);
        User seller = userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario não encontrado."));
        int number_followers = followerRepository.countBySellerId(seller);
        return mapQtdFollowersFromDTO(number_followers, seller);
    }

    public UserFollowersListDTO listFollowers(long UserId, String order){

        verification.checkUserId(UserId);
        User seller = userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario não encontrado."));
        List<Follower> relation = followerRepository.findBySellerId(seller);
        List<UserDTO> followers = new ArrayList<>(relation.stream()
                .map(this::mapRelationSellerBuyerDTO)
                .toList());


        switch (order){
            case "name_asc":
                followers.sort(Comparator.comparing(UserDTO::getUser_name));
                break;
            case "name_desc":
                followers.sort(Comparator.comparing(UserDTO::getUser_name).reversed());
                break;
        }

        return mapSellerEBuyerFromDTO(seller, followers);
    }

    public UserFollowerListDTO listFollowed(long UserId, String order){
        verification.checkUserId(UserId);
        User buyer = userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario não encontrado."));
        List<Follower> relation = followerRepository.findByBuyerId(buyer);
        List<UserDTO> followers = new ArrayList<>(relation.stream()
                .map(this::mapRelationBuyerSellerDTO)
                .toList());

        switch (order){
            case "name_asc":
                followers.sort(Comparator.comparing(UserDTO::getUser_name));
                break;
            case "name_desc":
                followers.sort(Comparator.comparing(UserDTO::getUser_name).reversed());
                break;
        }
        return mapBuyerESellerFromDTO(buyer, followers);
    }

    public void unfollow(long UserId, long UnfollowId){
        verification.checkUserId(UserId);
        User buyer = userRepository.findById(UserId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (comprador) não encontrado."));
        User seller = userRepository.findById(UnfollowId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuario (vendedor) não encontrado."));

        followerRepository.deleteByBuyerIdAndSellerId(buyer, seller);
    }


    private FollowersQuantityDTO mapQtdFollowersFromDTO(int numberFollowers, User seller){
        return new FollowersQuantityDTO(
            seller.getId(),
            seller.getUserName(),
            numberFollowers
        );

    }

    private UserDTO mapRelationSellerBuyerDTO(Follower relation) {

        User user = userRepository.findById(relation.getBuyerId().getId())
                .orElseThrow(() ->
                        new RuntimeException("Seguidor não encontrado: " + relation.getBuyerId().getId()));

        return new UserDTO(
                user.getId(),
                user.getUserName()
        );

    }

    private UserDTO mapRelationBuyerSellerDTO(Follower relation) {

        User user = userRepository.findById(relation.getSellerId().getId())
                .orElseThrow(() ->
                        new RuntimeException("Seguidor não encontrado: " + relation.getSellerId().getId()));

        return new UserDTO(
                user.getId(),
                user.getUserName()
        );
    }

    private UserFollowersListDTO mapSellerEBuyerFromDTO(User seller, List<UserDTO> buyers) {
        return new UserFollowersListDTO(
                seller.getId(),
                seller.getUserName(),
                buyers
        );
    }

    private UserFollowerListDTO mapBuyerESellerFromDTO(User buyer, List<UserDTO> sellers) {
        return new UserFollowerListDTO(
                buyer.getId(),
                buyer.getUserName(),
                sellers
        );
    }
}
