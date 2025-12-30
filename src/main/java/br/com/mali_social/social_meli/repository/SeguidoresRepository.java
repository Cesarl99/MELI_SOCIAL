package br.com.mali_social.social_meli.repository;

import br.com.mali_social.social_meli.entity.SeguidoresEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguidoresRepository extends JpaRepository<SeguidoresEntity, Long> {
    int countByVendedorId(long vendedorId);
    List<SeguidoresEntity> findByVendedorId(long vendedorId);
    List<SeguidoresEntity> findByCompradorId(long compradorId);
    @Transactional
    String deleteByCompradorIdAndVendedorId(long compradorId, long vendedorId);

}