package br.com.mali_social.social_meli.repository;



import br.com.mali_social.social_meli.entity.Publish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {
    List<Publish> findByUserIdInAndDateBetweenOrderByDateAsc(List<Long> usuarioIds, LocalDate dataInicial, LocalDate dataFinal);
    List<Publish> findByUserIdInAndDateBetweenOrderByDateDesc(List<Long> usuarioIds, LocalDate dataInicial, LocalDate dataFinal);
    int countByUserIdAndHasPromo(Long userId, boolean has_promo);
    List<Publish> findByUserIdAndHasPromoOrderByDateAsc(Long userId, boolean hasPromo);
}