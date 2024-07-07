package br.com.kayros.service;

import br.com.kayros.model.request.CreateUserRequest;
import br.com.kayros.model.request.UpdateUserRequest;
import br.com.kayros.model.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse findById(final String id);

    void save(CreateUserRequest request);

    List<UserResponse> findAll();

    UserResponse update(final String id, final UpdateUserRequest request);
}
