package com.tienda.unimagtienda.app.admin.dto;

import com.tienda.unimagtienda.security.domine.Role;

import java.util.Set;

public record AdminUserResponse(
        Long id,
        String email,
        Set<Role> roles,
         Boolean enabled
) {}