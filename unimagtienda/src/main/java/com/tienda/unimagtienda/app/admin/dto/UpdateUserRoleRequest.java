package com.tienda.unimagtienda.app.admin.dto;

import com.tienda.unimagtienda.security.domine.Role;

public record UpdateUserRoleRequest(
        Role role
) {}