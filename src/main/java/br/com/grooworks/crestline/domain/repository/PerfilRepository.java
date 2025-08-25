package br.com.grooworks.crestline.domain.repository;

import br.com.grooworks.crestline.domain.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, String> {
}
