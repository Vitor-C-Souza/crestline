package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.ListingDTO;
import br.com.grooworks.crestline.domain.model.Listing;
import br.com.grooworks.crestline.domain.repository.ListingRepository;
import br.com.grooworks.crestline.domain.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ListingServiceImpl implements ListingService {

    @Autowired
    private ListingRepository repository;


    @Override
    @Transactional(readOnly = true)
    public List<ListingDTO> findAll() {
        List<Listing> listings = repository.findAll();
        return listings.stream()
                .map(ListingDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ListingDTO getById(String id) {
        Listing listing = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Não foi encontrado List com esse id " + id));
        return new ListingDTO(listing);
    }

    @Override
    @Transactional
    public ListingDTO create(ListingDTO dto) {
        Listing listing = new Listing(dto);
        repository.save(listing);
        return new ListingDTO(listing);
    }

    @Override
    @Transactional
    public ListingDTO update(ListingDTO dto, String id) {
        Listing listing = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Não foi encontrado List com esse id " + id));
        listing.createOrUpdate(dto);
        repository.save(listing);
        return new ListingDTO(listing);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Não foi encontrado List com esse id " + id);
        }

        repository.deleteById(id);
    }
}
