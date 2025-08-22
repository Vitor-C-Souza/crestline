package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.ListingDTO;
import br.com.grooworks.crestline.domain.service.ListingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/listing")
@CrossOrigin(origins = "*")
public class ListingController {

    @Autowired
    private ListingService service;

    @GetMapping
    public ResponseEntity<List<ListingDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDTO> getById(@PathVariable("id") String id) {
        ListingDTO dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ListingDTO> create(@RequestBody @Valid ListingDTO dto, UriComponentsBuilder uriComponentsBuilder) {
        ListingDTO listingDTO = service.create(dto);
        URI uri = uriComponentsBuilder.path("/listing/{id}").buildAndExpand(listingDTO.id()).toUri();
        return ResponseEntity.created(uri).body(listingDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingDTO> update(@PathVariable("id") String id, @RequestBody @Valid ListingDTO dto) {
        ListingDTO update = service.update(dto, id);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
