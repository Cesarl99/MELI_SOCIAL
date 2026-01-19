package br.com.mali_social.social_meli.service;

import br.com.mali_social.social_meli.dto.product.ProductDTO;
import br.com.mali_social.social_meli.entity.Product;
import br.com.mali_social.social_meli.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product saveProduct(ProductDTO productDto){

        Product product = new Product();

        product.setProduct_name(productDto.getProduct_name());
        product.setType(productDto.getType());
        product.setBrand(productDto.getBrand());
        product.setColor(productDto.getColor());
        product.setNotes(productDto.getNotes());

        Product save = productRepository.save(product);
        Long idSave = save.getId();
        save.setId(idSave);
        return save;
    }

}
