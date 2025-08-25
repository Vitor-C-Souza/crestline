package br.com.grooworks.crestline.domain.model;

import br.com.grooworks.crestline.domain.dto.PerfilRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Entity
@Table(name = "Perfil")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String telefone;
    private String endereco;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] avatarUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Perfil(PerfilRequest dto, User user) {
        createOrUpdate(dto);
        this.user = user;
    }

    public void createOrUpdate(PerfilRequest dto) {
        this.firstName = dto.firstName();
        this.middleName = dto.middleName();
        this.lastName = dto.lastName();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
        this.avatarUrl = dto.avatarBase64() != null ? Base64.getDecoder().decode(dto.avatarBase64()) : null;

    }
}
