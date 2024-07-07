package br.com.kayros.model.response;

import br.com.kairos.model.enums.ProfileEnum;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

public record UserResponse(
    String id,
    String name,
    String email,
    String password,
    Set<ProfileEnum> profiles
) implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
}