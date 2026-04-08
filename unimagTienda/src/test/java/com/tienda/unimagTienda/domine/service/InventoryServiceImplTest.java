package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.InventoryDto.*;
import com.tienda.unimagTienda.domine.entity.Inventory;
import com.tienda.unimagTienda.domine.entity.Product;
import com.tienda.unimagTienda.domine.repository.InventoryRepository;
import com.tienda.unimagTienda.domine.repository.ProductRepository;
import com.tienda.unimagTienda.domine.service.mapper.InventoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInventory_shouldReturnInventoryResponse_whenProductExistsAndInventoryDoesNotExist() {
        // Given
        Long productId = 1L;
        InventoryRequest request = new InventoryRequest(productId, 100, 10);
        Product product = Product.builder().id(productId).name("Laptop").build();
        Inventory inventoryEntity = Inventory.builder().product(product).availableStock(100).minStock(10).build();
        Inventory savedInventoryEntity = Inventory.builder().id(1L).product(product).availableStock(100).minStock(10).build();
        InventoryResponse expectedResponse = new InventoryResponse(1L, productId, "Laptop", 100, 10, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());
        when(inventoryMapper.toEntity(request)).thenReturn(inventoryEntity);
        when(inventoryRepository.save(inventoryEntity)).thenReturn(savedInventoryEntity);
        when(inventoryMapper.toResponse(savedInventoryEntity)).thenReturn(expectedResponse);

        // When
        InventoryResponse actualResponse = inventoryService.create(request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.productId(), actualResponse.productId());
        assertEquals(expectedResponse.availableStock(), actualResponse.availableStock());
        verify(productRepository, times(1)).findById(productId);
        verify(inventoryRepository, times(1)).findByProductId(productId);
        verify(inventoryMapper, times(1)).toEntity(request);
        verify(inventoryRepository, times(1)).save(inventoryEntity);
        verify(inventoryMapper, times(1)).toResponse(savedInventoryEntity);
    }

    @Test
    void createInventory_shouldThrowRuntimeException_whenProductDoesNotExist() {
        // Given
        Long productId = 1L;
        InventoryRequest request = new InventoryRequest(productId, 100, 10);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.create(request));
        assertEquals("Product not found with id: " + productId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(inventoryRepository, never()).findByProductId(anyLong());
        verify(inventoryMapper, never()).toEntity(any());
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void createInventory_shouldThrowRuntimeException_whenInventoryAlreadyExistsForProduct() {
        // Given
        Long productId = 1L;
        InventoryRequest request = new InventoryRequest(productId, 100, 10);
        Product product = Product.builder().id(productId).build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(new Inventory()));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.create(request));
        assertEquals("Inventory already exists for product id: " + productId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(inventoryRepository, times(1)).findByProductId(productId);
        verify(inventoryMapper, never()).toEntity(any());
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void updateInventory_shouldReturnUpdatedInventoryResponse_whenInventoryAndProductExist() {
        // Given
        Long inventoryId = 1L;
        Long productId = 2L;
        InventoryRequest request = new InventoryRequest(productId, 150, 15);
        Product product = Product.builder().id(productId).name("New Laptop").build();
        Inventory existingInventory = Inventory.builder().id(inventoryId).product(Product.builder().id(1L).build()).build();
        Inventory savedInventory = Inventory.builder().id(inventoryId).product(product).availableStock(150).minStock(15).build();
        InventoryResponse expectedResponse = new InventoryResponse(inventoryId, productId, "New Laptop", 150, 15, null);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(existingInventory));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        doNothing().when(inventoryMapper).updateEntity(request, existingInventory);
        when(inventoryRepository.save(existingInventory)).thenReturn(savedInventory);
        when(inventoryMapper.toResponse(savedInventory)).thenReturn(expectedResponse);

        // When
        InventoryResponse actualResponse = inventoryService.update(inventoryId, request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.productId(), actualResponse.productId());
        assertEquals(expectedResponse.availableStock(), actualResponse.availableStock());
        verify(inventoryRepository, times(1)).findById(inventoryId);
        verify(productRepository, times(1)).findById(productId);
        verify(inventoryMapper, times(1)).updateEntity(request, existingInventory);
        verify(inventoryRepository, times(1)).save(existingInventory);
        verify(inventoryMapper, times(1)).toResponse(savedInventory);
    }

    @Test
    void getByProductId_shouldReturnInventoryResponse_whenExists() {
        // Given
        Long productId = 1L;
        Inventory inventoryEntity = Inventory.builder().id(1L).product(Product.builder().id(productId).build()).availableStock(100).build();
        InventoryResponse expectedResponse = new InventoryResponse(1L, productId, "Laptop", 100, 10, null);

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventoryEntity));
        when(inventoryMapper.toResponse(inventoryEntity)).thenReturn(expectedResponse);

        // When
        InventoryResponse actualResponse = inventoryService.getByProductId(productId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.productId(), actualResponse.productId());
        verify(inventoryRepository, times(1)).findByProductId(productId);
        verify(inventoryMapper, times(1)).toResponse(inventoryEntity);
    }

    @Test
    void getByProductId_shouldThrowRuntimeException_whenDoesNotExist() {
        // Given
        Long productId = 1L;
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.getByProductId(productId));
        assertEquals("Inventory not found for product id: " + productId, exception.getMessage());
        verify(inventoryRepository, times(1)).findByProductId(productId);
        verify(inventoryMapper, never()).toResponse(any());
    }

    @Test
    void updateStock_shouldIncreaseStock_whenQuantityIsPositive() {
        // Given
        Long productId = 1L;
        UpdateStockRequest request = new UpdateStockRequest(productId, 50);
        Inventory inventoryEntity = Inventory.builder().id(1L).availableStock(100).build();
        Inventory savedInventory = Inventory.builder().id(1L).availableStock(150).build();
        InventoryResponse expectedResponse = new InventoryResponse(1L, productId, "Laptop", 150, 10, null);

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventoryEntity));
        when(inventoryRepository.save(inventoryEntity)).thenReturn(savedInventory);
        when(inventoryMapper.toResponse(savedInventory)).thenReturn(expectedResponse);

        // When
        InventoryResponse actualResponse = inventoryService.updateStock(request);

        // Then
        assertEquals(150, inventoryEntity.getAvailableStock()); // Verify entity was updated
        assertEquals(expectedResponse.availableStock(), actualResponse.availableStock());
        verify(inventoryRepository, times(1)).findByProductId(productId);
        verify(inventoryRepository, times(1)).save(inventoryEntity);
    }

    @Test
    void updateStock_shouldDecreaseStock_whenQuantityIsNegativeAndEnoughStockExists() {
        // Given
        Long productId = 1L;
        UpdateStockRequest request = new UpdateStockRequest(productId, -50);
        Inventory inventoryEntity = Inventory.builder().id(1L).availableStock(100).build();
        Inventory savedInventory = Inventory.builder().id(1L).availableStock(50).build();
        InventoryResponse expectedResponse = new InventoryResponse(1L, productId, "Laptop", 50, 10, null);

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventoryEntity));
        when(inventoryRepository.save(inventoryEntity)).thenReturn(savedInventory);
        when(inventoryMapper.toResponse(savedInventory)).thenReturn(expectedResponse);

        // When
        InventoryResponse actualResponse = inventoryService.updateStock(request);

        // Then
        assertEquals(50, inventoryEntity.getAvailableStock()); // Verify entity was updated
        assertEquals(expectedResponse.availableStock(), actualResponse.availableStock());
        verify(inventoryRepository, times(1)).findByProductId(productId);
        verify(inventoryRepository, times(1)).save(inventoryEntity);
    }

    @Test
    void updateStock_shouldThrowRuntimeException_whenQuantityIsNegativeAndNotEnoughStockExists() {
        // Given
        Long productId = 1L;
        UpdateStockRequest request = new UpdateStockRequest(productId, -150);
        Inventory inventoryEntity = Inventory.builder().id(1L).availableStock(100).build();

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventoryEntity));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.updateStock(request));
        assertEquals("Not enough stock for product id: " + productId, exception.getMessage());
        verify(inventoryRepository, times(1)).findByProductId(productId);
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void getLowStock_shouldReturnListOfInventoryResponses() {
        // Given
        Inventory inv1 = Inventory.builder().id(1L).availableStock(5).minStock(10).build();
        Inventory inv2 = Inventory.builder().id(2L).availableStock(2).minStock(5).build();
        List<Inventory> inventoryEntities = Arrays.asList(inv1, inv2);

        InventoryResponse response1 = new InventoryResponse(1L, 1L, "P1", 5, 10, null);
        InventoryResponse response2 = new InventoryResponse(2L, 2L, "P2", 2, 5, null);

        when(inventoryRepository.findInventoriesWithLowStock()).thenReturn(inventoryEntities);
        when(inventoryMapper.toResponse(inv1)).thenReturn(response1);
        when(inventoryMapper.toResponse(inv2)).thenReturn(response2);

        // When
        List<InventoryResponse> actualResponses = inventoryService.getLowStock();

        // Then
        assertNotNull(actualResponses);
        assertEquals(2, actualResponses.size());
        verify(inventoryRepository, times(1)).findInventoriesWithLowStock();
        verify(inventoryMapper, times(2)).toResponse(any(Inventory.class));
    }
}
