package br.com.mali_social.social_meli.repository;

import br.com.mali_social.social_meli.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
