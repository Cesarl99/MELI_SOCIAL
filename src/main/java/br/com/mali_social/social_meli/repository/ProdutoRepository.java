package br.com.mali_social.social_meli.repository;


import br.com.mali_social.social_meli.entity.ProdutosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutosEntity, Long> {


}
