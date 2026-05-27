package com.tienda.unimagtienda.app.admin.service;

import com.tienda.unimagtienda.app.admin.dto.AdminDashboardResponse;
import com.tienda.unimagtienda.app.admin.dto.AdminUserResponse;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.app.product.repository.ProductRepository;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import com.tienda.unimagtienda.security.domine.AppUser;
import com.tienda.unimagtienda.security.domine.Role;
import com.tienda.unimagtienda.security.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public AdminDashboardResponse getDashboard() {

        long totalUsers = appUserRepository.count();
        long adminUsers = appUserRepository.countByRole(Role.ROLE_ADMIN);
        long normalUsers = totalUsers - adminUsers;
        long totalProducts = productRepository.count();
        long lowStockProducts = inventoryRepository.countLowStock();

        return new AdminDashboardResponse(
                totalUsers,
                adminUsers,
                normalUsers,
                totalProducts,
                lowStockProducts
        );
    }
    public List<AdminUserResponse> getAllUsers() {
        return appUserRepository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRoles(),
                        user.getEnabled()
                ))
                .toList();
    }

    public void updateUserRole(Long userId, Role role) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRoles(new HashSet<>(Set.of(role)));

        appUserRepository.save(user);
    }
    public void changeRole(Long userId, Role role) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.getRoles().clear();
        user.getRoles().add(role);

        appUserRepository.save(user);
    }
    @Transactional
    public void toggleUserStatus(Long id) {

        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEnabled(!user.getEnabled());

        appUserRepository.save(user);
    }
}