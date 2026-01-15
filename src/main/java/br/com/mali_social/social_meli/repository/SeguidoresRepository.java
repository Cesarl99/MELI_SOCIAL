package br.com.mali_social.social_meli.repository;

import br.com.mali_social.social_meli.entity.SeguidoresEntity;
import br.com.mali_social.social_meli.entity.UsuarioEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguidoresRepository extends JpaRepository<SeguidoresEntity, Long> {
    int countByVendedorId(UsuarioEntity vendedorId);
    List<SeguidoresEntity> findByVendedorId(UsuarioEntity vendedorId);
    List<SeguidoresEntity> findByCompradorId(UsuarioEntity compradorId);
    boolean existsByCompradorIdAndVendedorId(UsuarioEntity compradorId, UsuarioEntity vendedorId);
    @Transactional
    void deleteByCompradorIdAndVendedorId(UsuarioEntity compradorId, UsuarioEntity vendedorId);
}