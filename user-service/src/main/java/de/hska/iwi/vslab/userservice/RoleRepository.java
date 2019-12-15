package de.hska.iwi.vslab.userservice;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.hska.iwi.vslab.userservice.datamodels.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByLevel(int level);
}