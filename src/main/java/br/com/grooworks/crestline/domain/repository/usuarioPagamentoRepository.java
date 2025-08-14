package br.com.grooworks.crestline.domain.repository;

import br.com.grooworks.crestline.domain.model.UsuarioPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface usuarioPagamentoRepository extends JpaRepository<UsuarioPagamento, UUID> {
    Optional<UsuarioPagamento> findByUsuarioId(Long usuarioId);
}
