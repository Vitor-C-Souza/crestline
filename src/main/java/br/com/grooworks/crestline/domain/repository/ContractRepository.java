package br.com.grooworks.crestline.domain.repository;

import br.com.grooworks.crestline.domain.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, String> {
    Optional<Contract> findByEnvelopeId(String envelopeId);
}
