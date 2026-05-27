package com.tienda.unimagtienda.security.repository;

import com.tienda.unimagtienda.security.domine.AppUser;
import com.tienda.unimagtienda.security.domine.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);


    @Query("SELECT COUNT(u) FROM AppUser u WHERE :role MEMBER OF u.roles")
    long countByRole(@Param("role") Role role);
}