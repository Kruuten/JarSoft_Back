package com.kruten.jarsofttesttask.security.repository;

import java.util.Optional;

import com.kruten.jarsofttesttask.security.entity.ERole;
import com.kruten.jarsofttesttask.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
