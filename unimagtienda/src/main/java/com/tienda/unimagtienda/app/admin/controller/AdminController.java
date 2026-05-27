package com.tienda.unimagtienda.app.admin.controller;

import com.tienda.unimagtienda.app.admin.dto.AdminDashboardResponse;
import com.tienda.unimagtienda.app.admin.dto.AdminUserResponse;
import com.tienda.unimagtienda.app.admin.dto.UpdateUserRoleRequest;
import com.tienda.unimagtienda.app.admin.service.AdminService;
import com.tienda.unimagtienda.security.domine.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> dashboard() {
        return ResponseEntity.ok(adminService.getDashboard());
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleRequest req
    ) {
        adminService.updateUserRole(id, req.role());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/roles")
    public ResponseEntity<Role[]> getRoles() {
        return ResponseEntity.ok(Role.values());
    }
    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<Void> toggleStatus(@PathVariable Long id) {
        adminService.toggleUserStatus(id);
        return ResponseEntity.noContent().build();
    }
}