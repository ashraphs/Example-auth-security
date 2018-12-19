package com.example.Exampleauthsecurity.repositories;

import com.example.Exampleauthsecurity.entities.User;

import java.util.Optional;

public interface UserRepository extends MasterEntityRepository<User> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
