package br.com.grooworks.crestline.domain.repository;

import br.com.grooworks.crestline.domain.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerEntityRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByUsuarioId(Long usuarioId);
}
