package de.hska.iwi.vslab.authorizationservice.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.hska.iwi.vslab.authorizationservice.model.User;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}