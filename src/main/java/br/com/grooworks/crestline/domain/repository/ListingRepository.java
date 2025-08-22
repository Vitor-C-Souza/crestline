package br.com.grooworks.crestline.domain.repository;

import br.com.grooworks.crestline.domain.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, String> {
}
