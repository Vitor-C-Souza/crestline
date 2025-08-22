package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.ListingDTO;

import java.util.List;

public interface ListingService {
    List<ListingDTO> findAll();

    ListingDTO getById(String id);

    ListingDTO create(ListingDTO dto);

    ListingDTO update(ListingDTO dto, String id);

    void delete(String id);
}
