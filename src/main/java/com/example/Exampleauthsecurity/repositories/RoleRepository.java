package com.example.Exampleauthsecurity.repositories;

import com.example.Exampleauthsecurity.entities.role.Role;
import com.example.Exampleauthsecurity.entities.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
