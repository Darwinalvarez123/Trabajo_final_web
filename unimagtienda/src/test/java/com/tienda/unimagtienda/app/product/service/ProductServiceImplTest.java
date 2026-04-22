package com.tienda.unimagtienda.app.product.service;

import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.app.category.mapper.CategoryMapper;
import com.tienda.unimagtienda.app.category.repository.CategoryRepository;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.app.product.dto.CreateProductRequest;
import com.tienda.unimagtienda.app.product.dto.ProductResponse;
import com.tienda.unimagtienda.app.product.dto.UpdateProductRequest;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.app.product.mapper.ProductMapper;
import com.tienda.unimagtienda.app.product.repository.ProductRepository;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mapstruct.factory.Mappers;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private InventoryRepository inventoryRepository;

    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Spy
    private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productMapper, "categoryMapper", categoryMapper);
    }

    @Test
    @DisplayName("ProductService: crea producto e inventario correctamente")
    void shouldCreateProductSuccessfully() {

        var req = new CreateProductRequest(
                "SKU123", "Producto", "Desc",
                BigDecimal.valueOf(100), 1L
        );

        Category category = Category.builder().id(1L).build();

        when(productRepository.existsBySku("SKU123")).thenReturn(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(productRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        ProductResponse res = service.create(req);

        assertThat(res.id()).isEqualTo(10L);

        verify(productRepository).save(any(Product.class));
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    @DisplayName("ProductService: falla si SKU ya existe")
    void shouldFailIfSkuExists() {

        var req = new CreateProductRequest(
                "SKU123", "Producto", "Desc",
                BigDecimal.valueOf(100), 1L
        );

        when(productRepository.existsBySku("SKU123")).thenReturn(true);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);

        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("ProductService: falla si precio <= 0")
    void shouldFailIfPriceInvalid() {

        var req = new CreateProductRequest(
                "SKU123", "Producto", "Desc",
                BigDecimal.ZERO, 1L
        );

        when(productRepository.existsBySku("SKU123")).thenReturn(false);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("ProductService: obtiene producto activo por id")
    void shouldGetById() {

        Product product = Product.builder()
                .id(1L)
                .sku("SKU123")
                .active(true)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse res = service.getById(1L);

        assertThat(res.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("ProductService: lanza excepción si no existe o está inactivo")
    void shouldThrowExceptionWhenProductNotFoundOrInactive() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class);

        Product inactiveProduct = Product.builder()
                .id(2L)
                .active(false)
                .build();

        when(productRepository.findById(2L)).thenReturn(Optional.of(inactiveProduct));

        assertThatThrownBy(() -> service.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("ProductService: actualiza producto correctamente (sin modificar SKU)")
    void shouldUpdateProduct() {

        Category category = Category.builder().id(2L).build();

        Product product = Product.builder()
                .id(1L)
                .sku("OLD")
                .active(true)
                .name("Viejo")
                .price(BigDecimal.valueOf(100))
                .build();

        var req = new UpdateProductRequest(
                "NEW", // ❌ ignorado por mapper
                "Nuevo",
                null,
                BigDecimal.valueOf(200),
                2L
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsBySku("NEW")).thenReturn(false);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductResponse res = service.update(1L, req);

        assertThat(res).isNotNull();

        // ✔ campos que sí cambian
        assertThat(product.getName()).isEqualTo("Nuevo");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(200));
        assertThat(product.getCategory()).isEqualTo(category);

        // ✔ SKU NO cambia
        assertThat(product.getSku()).isEqualTo("OLD");

        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("ProductService: elimina producto (soft delete)")
    void shouldDeleteProduct() {

        Product product = Product.builder()
                .id(1L)
                .active(true)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        assertThat(product.getActive()).isFalse();
        verify(productRepository).save(product);
    }
}