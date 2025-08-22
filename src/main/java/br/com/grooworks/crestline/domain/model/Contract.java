package br.com.grooworks.crestline.domain.model;

import br.com.grooworks.crestline.domain.dto.SendContractDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "Contract")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;
    private String description;

    private String signerName;
    private String signerEmail;

    private String envelopeId;
    private String documentId;

    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public Contract(SendContractDto dto, String envelopeId) {
        this.title = dto.title();
        this.description = dto.description();
        this.signerName = dto.signerName();
        this.signerEmail = dto.signerEmail();
        this.envelopeId = envelopeId;
        this.documentId = "1";
        this.status = "Sent";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
