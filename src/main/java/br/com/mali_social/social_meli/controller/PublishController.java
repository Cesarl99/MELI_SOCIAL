package br.com.mali_social.social_meli.controller;

import br.com.mali_social.social_meli.dto.publish.PublishDiscountUserListDTO;
import br.com.mali_social.social_meli.dto.publish.PublishUserListDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDTO;
import br.com.mali_social.social_meli.dto.publish.PublishDiscountQuantityDTO;
import br.com.mali_social.social_meli.entity.Product;
import br.com.mali_social.social_meli.service.ProductService;
import br.com.mali_social.social_meli.service.PublishService;
import br.com.mali_social.social_meli.util.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("products")
@CrossOrigin(origins = "http://localhost:5173")
public class PublishController {

    @Autowired
    private PublishService publishService;
    @Autowired
    private ProductService productService;

    private final Verification verification = new Verification();

    @PostMapping("/publish")
    public void savePublish(@RequestBody PublishDTO publishDTO){
        verification.checkPublicationRegistration(publishDTO);
        Product product = productService.saveProduct(publishDTO.getProduct());
        publishService.savePublish(publishDTO, product);
    }

    @GetMapping("/followed/{userId}/list")
    public PublishUserListDTO listPublishUser(@PathVariable Long userId,
                                                     @RequestParam(name = "order", required = false, defaultValue = "date_asc") String order){
        return publishService.listPublishUser(userId, order);
    }

    @PostMapping ("/promo-pub")
    public void savePublishDiscount(@RequestBody PublishDTO publishDTO){
        Product product = productService.saveProduct(publishDTO.getProduct());
        String ret = publishService.savePublish(publishDTO, product);
    }

    @GetMapping ("/promo-pub/count")
    public PublishDiscountQuantityDTO countPublishDiscount (@RequestParam (name = "user_id", required = true) Long user_id){
        return publishService.countPublishDiscount(user_id);
    }

    @GetMapping ("/promo-pub/list")
    public PublishDiscountUserListDTO listPromotionUser (@RequestParam(name = "user_id", required = true) Long userId){
        return publishService.listPublishUserDiscount(userId);
    }



}
