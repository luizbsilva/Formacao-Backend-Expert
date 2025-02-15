package br.com.kayros.repository;

import br.com.kayros.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(final String email);

  void deleteByEmail(final String validEmail);
}
