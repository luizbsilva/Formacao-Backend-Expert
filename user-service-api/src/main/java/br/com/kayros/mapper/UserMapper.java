package br.com.kayros.mapper;

import br.com.kayros.entity.User;
import br.com.kayros.model.request.CreateUserRequest;
import br.com.kayros.model.request.UpdateUserRequest;
import br.com.kayros.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = IGNORE,
    nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {

  UserResponse fromEntity(final User entity);

  @Mapping(target = "id", ignore = true)
  User fromRequest(CreateUserRequest createUserRequest);

  @Mapping(target = "id", ignore = true)
  User update(UpdateUserRequest updateUserRequest, @MappingTarget User entity);
}


