package br.com.grooworks.crestline.controller;

import br.com.grooworks.crestline.domain.dto.ListingDTO;
import br.com.grooworks.crestline.domain.service.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Listar todos os listings",
            tags = {"Listing"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de listings retornada",
                            content = @Content(schema = @Schema(implementation = ListingDTO.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<ListingDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Buscar listing por ID",
            tags = {"Listing"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listing encontrado",
                            content = @Content(schema = @Schema(implementation = ListingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Listing não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ListingDTO> getById(@PathVariable("id") String id) {
        ListingDTO dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Criar novo listing",
            tags = {"Listing"},
            requestBody = @RequestBody(
                    description = "Dados do novo listing",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ListingDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "streetAdress": "123 Main St",
                                              "city": "Los Angeles",
                                              "state": "CA",
                                              "zipCode": "90001",
                                              "county": "Los Angeles County",
                                              "electricity": true,
                                              "water": true,
                                              "sewer": false,
                                              "naturalGas": true,
                                              "highSpeedInternet": true,
                                              "phoneService": false,
                                              "pavedRoadAccess": true,
                                              "gatedCommunity": false,
                                              "mountainViews": true,
                                              "waterViews": false,
                                              "wooded": true,
                                              "openPasture": false,
                                              "creekStream": false,
                                              "pond": true,
                                              "wellOnProperty": true,
                                              "septicSystem": false,
                                              "fenced": true,
                                              "barnOutbuildings": false,
                                              "mineralRights": false,
                                              "huntingRights": true,
                                              "propertyDescription": "Beautiful property with mountain views",
                                              "listingPrice": "250000.00",
                                              "pricePerAcre": "15000.00",
                                              "downPayment": "50000.00",
                                              "monthlyPayment": "1200.00",
                                              "paymentTerms": "Owner financing available",
                                              "profileProperties": "<base64-string>"
                                            }"""
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Listing criado",
                            content = @Content(schema = @Schema(implementation = ListingDTO.class)))
            }
    )
    @PostMapping
    public ResponseEntity<ListingDTO> create(
            @RequestBody @Valid ListingDTO dto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        ListingDTO listingDTO = service.create(dto);
        URI uri = uriComponentsBuilder.path("/listing/{id}").buildAndExpand(listingDTO.id()).toUri();
        return ResponseEntity.created(uri).body(listingDTO);
    }

    @Operation(
            summary = "Atualizar listing existente",
            tags = {"Listing"},
            requestBody = @RequestBody(
                    description = "Dados atualizados do listing",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ListingDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listing atualizado",
                            content = @Content(schema = @Schema(implementation = ListingDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Listing não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ListingDTO> update(@PathVariable("id") String id, @RequestBody @Valid ListingDTO dto) {
        ListingDTO update = service.update(dto, id);
        return ResponseEntity.ok(update);
    }

    @Operation(
            summary = "Deletar listing por ID",
            tags = {"Listing"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Listing deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Listing não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
