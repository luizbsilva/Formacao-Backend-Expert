package br.com.kayros.model.request;

import br.com.kayros.model.enums.ProfileEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.With;

@With
public record UpdateUserRequest(
    @Schema(description = "User name")
    @Size(min = 3, max = 50, message = "Name must contain between 3 and 50 characters")
    String name,

    @Schema(description = "User email", example = "exxemple@mail.com")
    @Email(message = "Invalid email")
    @Size(min = 6, max = 50, message = "Email must contain between 6 and 50 characters")
    String email,

    @Schema(description = "User password", example = "123456")
    @Size(min = 6, max = 50, message = "Password must contain between 6 and 50 characters")
    String password,

    @Schema(description = "User profiles", example = "[\"ROLE_ADMIN\", \"ROLE_CUSTOMER\"]")
    Set<ProfileEnum> profiles
) {

}