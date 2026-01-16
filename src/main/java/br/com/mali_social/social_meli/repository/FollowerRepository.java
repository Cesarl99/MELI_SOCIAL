package br.com.mali_social.social_meli.repository;

import br.com.mali_social.social_meli.entity.Follower;
import br.com.mali_social.social_meli.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    int countBySellerId(User vendedorId);
    List<Follower> findBySellerId(User sellerId);
    List<Follower> findByBuyerId(User buyerId);
    boolean existsByBuyerIdAndSellerId(User buyerId, User sellerId);
    @Transactional
    void deleteByBuyerIdAndSellerId(User buyerId, User sellerId);
}