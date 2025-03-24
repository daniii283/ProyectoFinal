package com.newtonbox.Repository;

import com.newtonbox.Models.Role;
import com.newtonbox.utils.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleEnum (RoleEnum roleEnum);
}
