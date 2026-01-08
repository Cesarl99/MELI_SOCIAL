package br.com.mali_social.social_meli.repository;



import br.com.mali_social.social_meli.entity.PublicacaoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublicacaoRepository extends JpaRepository<PublicacaoEntity, Long> {
    List<PublicacaoEntity> findByUsuarioIdInAndDataBetweenOrderByDataAsc(List<Long> usuarioIds, LocalDate dataInicial, LocalDate dataFinal);
    List<PublicacaoEntity> findByUsuarioIdInAndDataBetweenOrderByDataDesc(List<Long> usuarioIds,LocalDate dataInicial, LocalDate dataFinal);
    int countByUsuarioIdAndPromocao(Long usuarioId, boolean promocao);
    List<PublicacaoEntity> findByUsuarioIdAndPromocaoOrderByDataAsc(Long usuarioId, boolean promocao);
}