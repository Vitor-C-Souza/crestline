package br.com.grooworks.crestline.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PerfilRequest(
        @NotBlank(message = "O primeiro nome é obrigatório")
        @Size(max = 100, message = "O primeiro nome deve ter no máximo 100 caracteres")
        String firstName,

        @Size(max = 100, message = "O nome do meio deve ter no máximo 100 caracteres")
        String middleName,

        @NotBlank(message = "O sobrenome é obrigatório")
        @Size(max = 100, message = "O sobrenome deve ter no máximo 100 caracteres")
        String lastName,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "O telefone contém caracteres inválidos")
        String telefone,

        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
        String endereco,

        // Pode ser nulo, mas se vier precisa estar em Base64 válido
        @Pattern(
                regexp = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$",
                message = "Avatar deve estar em formato Base64 válido"
        )
        String avatarBase64,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email
) {
}
