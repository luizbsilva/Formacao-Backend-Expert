package br.com.kayros.model.enums;


import static lombok.AccessLevel.PRIVATE;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = PRIVATE)
public enum ProfileEnum {

  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_CUSTOMER("ROLE_CUSTOMER"),
  ROLE_TECHNICIAN("ROLE_TECHNICIAN");

  @Getter
  private final String description;

  public static ProfileEnum toEnum(final String description) {
    return Arrays.stream(ProfileEnum.values())
        .filter(profileEnum -> profileEnum.getDescription().equals(description))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid description: " + description));
  }
}