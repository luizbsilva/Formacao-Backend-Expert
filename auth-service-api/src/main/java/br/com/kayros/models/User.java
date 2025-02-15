package br.com.kayros.models;

import br.com.kayros.model.enums.ProfileEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class User {
  private String id;
  private String name;
  private String email;
  private String password;
  private Set<ProfileEnum> profiles;
}

