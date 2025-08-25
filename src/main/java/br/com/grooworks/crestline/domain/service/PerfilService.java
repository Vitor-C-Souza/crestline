package br.com.grooworks.crestline.domain.service;

import br.com.grooworks.crestline.domain.dto.PerfilRequest;
import br.com.grooworks.crestline.domain.dto.PerfilResponse;

import java.util.List;

public interface PerfilService {
    PerfilResponse create(PerfilRequest dto);

    List<PerfilResponse> getAll();

    PerfilResponse getById(String id);

    PerfilResponse update(String id, PerfilRequest dto);

    void delete(String id);
}
