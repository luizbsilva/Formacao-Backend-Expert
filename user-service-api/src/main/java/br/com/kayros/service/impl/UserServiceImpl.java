package br.com.kayros.service.impl;

import br.com.kayros.entity.User;
import br.com.kayros.mapper.UserMapper;
import br.com.kayros.model.exceptions.ResourceNotFoundException;
import br.com.kayros.model.request.CreateUserRequest;
import br.com.kayros.model.request.UpdateUserRequest;
import br.com.kayros.model.response.UserResponse;
import br.com.kayros.repository.UserRepository;
import br.com.kayros.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UserResponse findById(final String id) {
        return mapper.fromEntity(find(id));
    }

    @Override
    public void save(CreateUserRequest request) {
        verifyIfEmailAlreadyExists(request.email(), null);
        repository.save(
                mapper.fromRequest(request)
                        .withPassword(encoder.encode(request.password()))
        );
    }

    @Override
    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream().map(mapper::fromEntity)
                .toList();
    }

    @Override
    public UserResponse update(final String id, final UpdateUserRequest request) {
        User entity = find(id);
        verifyIfEmailAlreadyExists(request.email(), id);
        return mapper.fromEntity(
                repository.save(
                        mapper.update(request, entity)
                                .withPassword(request.password() != null ? encoder.encode(request.password())
                                        : entity.getPassword())
                )
        );
    }

    private User find(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                ));
    }

    private void verifyIfEmailAlreadyExists(final String email, final String id) {
        repository.findByEmail(email)
                .filter(user -> !user.getId().equals(id))
                .ifPresent(user -> {
                    throw new DataIntegrityViolationException("Email [ " + email + " ] already exists");
                });
    }

}
