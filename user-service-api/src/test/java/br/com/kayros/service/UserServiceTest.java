package br.com.kayros.service;

import static br.com.kayros.creator.CreatorUtils.generateMock;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.kayros.entity.User;
import br.com.kayros.mapper.UserMapper;
import br.com.kayros.model.exceptions.ResourceNotFoundException;
import br.com.kayros.model.request.CreateUserRequest;
import br.com.kayros.model.request.UpdateUserRequest;
import br.com.kayros.model.response.UserResponse;
import br.com.kayros.repository.UserRepository;
import br.com.kayros.service.impl.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {

  @InjectMocks
  private UserServiceImpl service;

  @Mock
  private UserRepository repository;

  @Mock
  private UserMapper mapper;

  @Mock
  private BCryptPasswordEncoder encoder;

  @Test
  void whenCallFindByIdWithValidIdThenReturnUserResponse() {
    when(repository.findById(anyString())).thenReturn(Optional.of(new User()));
    when(mapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

    final var response = service.findById("1");

    assertNotNull(response);
    assertEquals(UserResponse.class, response.getClass());

    verify(repository).findById(anyString());
    verify(mapper).fromEntity(any(User.class));
  }

  @Test
  void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
    when(repository.findById(anyString())).thenReturn(Optional.empty());

    assertAll(
        () -> assertThrows(ResourceNotFoundException.class, () -> service.findById("1"))
    );

    verify(repository).findById(anyString());
    verify(mapper, times(0)).fromEntity(any(User.class));
  }

  @Test
  void whenCallFindAllThenReturnListOfUserResponse() {
    when(repository.findAll()).thenReturn(List.of(new User(), new User()));
    when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));

    final var response = service.findAll();

    assertNotNull(response);
    assertEquals(2, response.size());
    assertEquals(UserResponse.class, response.get(0).getClass());

    verify(repository, times(1)).findAll();
    verify(mapper, times(2)).fromEntity(any(User.class));
  }

  @Test
  void whenCallSaveThenSuccess() {
    final var request = generateMock(CreateUserRequest.class);

    when(mapper.fromRequest(any())).thenReturn(new User());
    when(encoder.encode(anyString())).thenReturn("encoded");
    when(repository.save(any(User.class))).thenReturn(new User());
    when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

    service.save(request);

    verify(mapper).fromRequest(request);
    verify(encoder).encode(request.password());
    verify(repository).save(any(User.class));
    verify(repository).findByEmail(request.email());
  }

  @Test
  void whenCallSaveWithInvalidEmailThenThrowDataIntegrityViolationException() {
    final var request = generateMock(CreateUserRequest.class);
    final var entity = generateMock(User.class);

    when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));

    try {
      service.save(request);
    } catch (Exception e) {
      assertEquals(DataIntegrityViolationException.class, e.getClass());
      assertEquals("Email [ " + request.email() + " ] already exists", e.getMessage());
    }

    verify(repository).findByEmail(request.email());
    verify(mapper, times(0)).fromRequest(request);
    verify(encoder, times(0)).encode(request.password());
    verify(repository, times(0)).save(any(User.class));
  }

  @Test
  void whenCallUpdateWithInvalidIdThenThrowResourceNotFoundException() {
    final var request = generateMock(UpdateUserRequest.class);

    when(repository.findById(anyString())).thenReturn(Optional.empty());

    assertAll(
        () -> assertThrows(ResourceNotFoundException.class, () -> service.update("1", request))
    );
    verify(repository).findById(anyString());
    verify(mapper, times(0)).update(any(), any());
    verify(encoder, times(0)).encode(request.password());
    verify(repository, times(0)).save(any(User.class));
  }

  @Test
  void whenCallUpdateWithInvalidEmailThenThrowDataIntegrityViolationException() {
    final var request = generateMock(UpdateUserRequest.class);
    final var entity = generateMock(User.class);

    when(repository.findById(anyString())).thenReturn(Optional.of(entity));
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));

    try {
      service.update("1", request);
    } catch (Exception e) {
      assertEquals(DataIntegrityViolationException.class, e.getClass());
      assertEquals("Email [ " + request.email() + " ] already exists", e.getMessage());
    }

    verify(repository).findById(anyString());
    verify(repository).findByEmail(request.email());
    verify(mapper, times(0)).update(any(), any());
    verify(encoder, times(0)).encode(request.password());
    verify(repository, times(0)).save(any(User.class));
  }

  @Test
  void whenCallUpdateWithValidParamsThenGetSuccess() {
    final var id = "1";
    final var request = generateMock(UpdateUserRequest.class);
    final var entity = generateMock(User.class).withId(id);

    when(repository.findById(anyString())).thenReturn(Optional.of(entity));
    when(repository.findByEmail(anyString())).thenReturn(Optional.of(entity));
    when(mapper.update(any(), any())).thenReturn(entity);
    when(repository.save(any(User.class))).thenReturn(entity);

    service.update(id, request);

    verify(repository).findById(anyString());
    verify(repository).findByEmail(request.email());
    verify(mapper).update(request, entity);
    verify(encoder).encode(request.password());
    verify(repository).save(any(User.class));
    verify(mapper).fromEntity(any(User.class));
  }

}