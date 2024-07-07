package br.com.kayros.entity;

import br.com.kayros.model.enums.ProfileEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@With
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Set<ProfileEnum> profiles;
}