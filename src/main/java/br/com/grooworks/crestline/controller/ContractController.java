package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import br.com.grooworks.crestline.domain.model.Contract;
import br.com.grooworks.crestline.domain.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contract")
@CrossOrigin(origins = "*")
public class ContractController {

    @Autowired
    private ContractService service;

    @GetMapping("/{id}")
    public ResponseEntity<Contract> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<List<Contract>> list() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping("/send")
    public ResponseEntity<Contract> send(@RequestBody SendContractDto dto, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        Contract contract = service.createAndSend(dto);
        URI uri = uriComponentsBuilder.path("/contract/{id}").buildAndExpand(contract.getId()).toUri();
        return ResponseEntity.created(uri).body(contract);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable("id") String id) throws Exception {
        byte[] pdf = service.download(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/{id}/document")
    public ResponseEntity<Map<String, Object>> getDoc(@PathVariable String id) throws Exception {
        byte[] pdf = service.download(id);
        return ResponseEntity.ok(Map.of("contractId", id, "base64", Base64.getEncoder().encodeToString(pdf)));
    }

    @PatchMapping("/{id}/status")
    public Contract updateStatus(@PathVariable("id") String id) throws Exception {
        return service.checkStatus(id);
    }

    @PostMapping("/{id}/resend")
    public ResponseEntity<Void> resend(@PathVariable("id") String id) throws Exception {
        service.resend(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
