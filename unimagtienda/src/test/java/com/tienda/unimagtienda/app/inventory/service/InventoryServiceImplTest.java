package com.tienda.unimagtienda.app.inventory.service;

import com.tienda.unimagtienda.app.inventory.dto.UpdateInventoryRequest;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.inventory.mapper.InventoryMapper;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository repository;

    @Spy
    private InventoryMapper mapper = Mappers.getMapper(InventoryMapper.class);

    @InjectMocks
    private InventoryServiceImpl service;

    // ---------- GET ----------

    @Test
    @DisplayName("getByProductId: retorna inventario")
    void shouldGetByProductId() {
        var product = Product.builder().id(1L).build();

        var entity = Inventory.builder()
                .id(10L)
                .product(product)
                .availableStock(20)
                .minStock(5)
                .build();

        when(repository.findByProductId(1L)).thenReturn(Optional.of(entity));

        var res = service.getByProductId(1L);

        assertThat(res.productId()).isEqualTo(1L);
        assertThat(res.availableStock()).isEqualTo(20);
    }

    @Test
    @DisplayName("getByProductId: error si no existe")
    void shouldThrowIfNotFound() {
        when(repository.findByProductId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByProductId(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ---------- UPDATE ----------

    @Test
    @DisplayName("update: actualiza correctamente")
    void shouldUpdateInventory() {
        var product = Product.builder().id(1L).build();

        var entity = Inventory.builder()
                .id(10L)
                .product(product)
                .availableStock(20)
                .minStock(5)
                .build();

        var req = new UpdateInventoryRequest(30, 10);

        when(repository.findByProductId(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(Inventory.class))).thenReturn(entity);

        var res = service.updateByProductId(1L, req);

        assertThat(res.availableStock()).isEqualTo(30);
        assertThat(res.minStock()).isEqualTo(10);

        verify(repository).save(entity);
    }

    @Test
    @DisplayName("update: error si availableStock negativo")
    void shouldThrowIfAvailableStockNegative() {
        var product = Product.builder().id(1L).build();

        var entity = Inventory.builder()
                .id(10L)
                .product(product)
                .availableStock(20)
                .minStock(5)
                .build();

        var req = new UpdateInventoryRequest(-1, 5);

        when(repository.findByProductId(1L)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.updateByProductId(1L, req))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("update: error si minStock negativo")
    void shouldThrowIfMinStockNegative() {
        var product = Product.builder().id(1L).build();

        var entity = Inventory.builder()
                .id(10L)
                .product(product)
                .availableStock(20)
                .minStock(5)
                .build();

        var req = new UpdateInventoryRequest(10, -5);

        when(repository.findByProductId(1L)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.updateByProductId(1L, req))
                .isInstanceOf(ConflictException.class);
    }
}