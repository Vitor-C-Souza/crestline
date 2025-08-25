package br.com.grooworks.crestline.domain.service.impl;

import br.com.grooworks.crestline.domain.dto.PerfilRequest;
import br.com.grooworks.crestline.domain.dto.PerfilResponse;
import br.com.grooworks.crestline.domain.model.Perfil;
import br.com.grooworks.crestline.domain.model.User;
import br.com.grooworks.crestline.domain.repository.PerfilRepository;
import br.com.grooworks.crestline.domain.repository.UserRepository;
import br.com.grooworks.crestline.domain.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerfilServiceImpl implements PerfilService {

    @Autowired
    private PerfilRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public PerfilResponse create(PerfilRequest dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new NoSuchElementException("Usuario não encontrado com esse email " + dto.email()));
        Perfil perfil = new Perfil(dto, user);
        repository.save(perfil);
        return new PerfilResponse(perfil);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilResponse> getAll() {
        return repository.findAll().stream()
                .map(PerfilResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilResponse getById(String id) {
        Perfil perfil = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Perfil não encontrado com esse id " + id));

        return new PerfilResponse(perfil);
    }

    @Override
    @Transactional
    public PerfilResponse update(String id, PerfilRequest dto) {
        Perfil perfil = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Perfil não encontrado com esse id " + id));
        perfil.createOrUpdate(dto);
        repository.save(perfil);
        return new PerfilResponse(perfil);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Optional<Perfil> perfilOptional = repository.findById(id);
        if (perfilOptional.isEmpty()) {
            throw new NoSuchElementException("Perfil não encontrado com esse id " + id);
        }


        userRepository.deleteById(perfilOptional.get().getUser().getId());
    }
}
