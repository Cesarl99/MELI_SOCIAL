package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.product.ProductDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDiscountUserListDTO;
import br.com.mali_social.social_meli.dto.publish.PublishUserListDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDiscountQuantityDTO;
import br.com.mali_social.social_meli.entity.Product;
import br.com.mali_social.social_meli.entity.Publish;
import br.com.mali_social.social_meli.entity.Follower;
import br.com.mali_social.social_meli.entity.User;
import br.com.mali_social.social_meli.repository.PublishRepository;
import br.com.mali_social.social_meli.repository.FollowerRepository;
import br.com.mali_social.social_meli.repository.UserRepository;
import br.com.mali_social.social_meli.util.Verification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class PublishService {
    private final PublishRepository publishRepository;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final ProductService productService;
    private final Verification verification = new Verification();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public PublishService(PublishRepository publishRepository, UserRepository userRepository, FollowerRepository followerRepository, ProductService productService){
        this.publishRepository = publishRepository;
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
        this.productService = productService;
    }

    public String savePublish(PublishDTO publishDTO, Product product){
        Publish publish = new Publish();
        if( publishDTO.getDiscount() >= publishDTO.getPrice()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do desconto não pode ser superior ou igual ao valor do produto.");
        }

        publishDTO.getProduct().setProduct_id(product.getId());
        verification.checkPublicationRegistration(publishDTO);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        publish.setDate(LocalDate.parse(publishDTO.getDate(),format));
        publish.setCategory(publishDTO.getCategory());
        publish.setPrice(publishDTO.getPrice());
        publish.setHas_promo(publishDTO.isHas_promo());
        publish.setDiscount(publishDTO.getDiscount());

        User userProduct = userRepository.findById(publishDTO.getUser_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não encontrado"));
        publish.setUser(userProduct);

        publish.setProduct(product);

        return (publishRepository.save(publish).toString());
    }

    public PublishUserListDTO listPublishUser(Long user_id, String order) {

        verification.checkUserId(user_id);
        User buyer = userRepository.findById(user_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não encontrado"));

        List<Follower> relation = followerRepository.findByBuyerId(buyer);

        LocalDate today = LocalDate.now();
        LocalDate dateLimit = today.minusWeeks(2);

        List<Long> idsSellers = relation.stream()
                .map(Follower::getSellerId)
                .map(User::getId)
                .distinct()
                .toList();

        List<Publish> publishEnt = new ArrayList<>();
        List<PublishDTO> publications= new ArrayList<>();

        if ("date_desc".equals(order)){
            publishEnt = publishRepository.findByUserIdInAndDateBetweenOrderByDateDesc(idsSellers,dateLimit, today);
        } else {
            publishEnt = publishRepository.findByUserIdInAndDateBetweenOrderByDateAsc(idsSellers, dateLimit, today);
        }

        publications = toDtoList(publishEnt);
        return mapPublishUser(user_id, publications);
    }

    public PublishDiscountQuantityDTO countPublishDiscount(Long user_id){
        verification.checkUserId(user_id);
        User seller = userRepository.findById(user_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não encontrado"));
        int quantityPublish = publishRepository.countByUserIdAndHasPromo(user_id, true);

        return new PublishDiscountQuantityDTO(
                user_id,
                seller.getUserName(),
                quantityPublish
        );
    }

    public PublishDiscountUserListDTO listPublishUserDiscount(Long user_id){
        verification.checkUserId(user_id);
        User seller = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        List<Publish> publishesEnt = publishRepository.findByUserIdAndHasPromoOrderByDateAsc(user_id, true);
        return new PublishDiscountUserListDTO(
                user_id,
                seller.getUserName(),
                toDtoList(publishesEnt)
        );
    }

    private PublishDTO toDto(Publish entity) {
        if (entity == null) return null;

        Product prod = entity.getProduct();
        ProductDTO productDto = null;
        if (prod != null) {
            productDto = new ProductDTO(
                    prod.getNotes(),
                    prod.getColor(),
                    prod.getBrand(),
                    prod.getId(),
                    prod.getType(),
                    prod.getProduct_name()
            );
        }

        return new PublishDTO(
                entity.getId(),
                entity.getUser().getId(),
                entity.getDate().format(FORMATTER),
                productDto,
                entity.getCategory(),
                entity.getPrice(),
                entity.isHas_promo(),
                entity.getDiscount()
        );
    }

    private List<PublishDTO> toDtoList(List<Publish> entities) {

        List<PublishDTO> dtos = new ArrayList<>();

        if (entities == null) {
            return dtos;
        }

        for (Publish entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }


    private PublishUserListDTO mapPublishUser(long UserId, List<PublishDTO> publications ){
        return new PublishUserListDTO(
            UserId,
            publications
        );
    }

}
