package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.ProductDto.*;
import com.tienda.unimagTienda.domine.entity.Category;
import com.tienda.unimagTienda.domine.entity.Product;
import com.tienda.unimagTienda.domine.repository.CategoryRepository;
import com.tienda.unimagTienda.domine.repository.ProductRepository;
import com.tienda.unimagTienda.domine.service.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_shouldReturnProductResponse_whenSkuAndCategoryExist() {
        // Given
        Long categoryId = 1L;
        CreateProductRequest request = new CreateProductRequest("SKU001", "Laptop", "Powerful laptop", new BigDecimal("1200.00"), categoryId);
        Category category = Category.builder().id(categoryId).name("Electronics").build();
        Product productEntity = Product.builder()
                .sku("SKU001").name("Laptop").description("Powerful laptop").price(new BigDecimal("1200.00")).category(category)
                .build();
        Product savedProductEntity = Product.builder()
                .id(1L).sku("SKU001").name("Laptop").description("Powerful laptop").price(new BigDecimal("1200.00")).category(category).active(true)
                .build();
        ProductResponse expectedResponse = new ProductResponse(1L, "SKU001", "Laptop", "Powerful laptop", new BigDecimal("1200.00"), true, categoryId);

        when(productRepository.findBySku(request.sku())).thenReturn(Optional.empty());
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(request)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(savedProductEntity);
        when(productMapper.toResponse(savedProductEntity)).thenReturn(expectedResponse);

        // When
        ProductResponse actualResponse = productService.create(request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.sku(), actualResponse.sku());
        verify(productRepository, times(1)).findBySku(request.sku());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productMapper, times(1)).toEntity(request);
        verify(productRepository, times(1)).save(productEntity);
        verify(productMapper, times(1)).toResponse(savedProductEntity);
    }

    @Test
    void createProduct_shouldThrowRuntimeException_whenSkuAlreadyExists() {
        // Given
        Long categoryId = 1L;
        CreateProductRequest request = new CreateProductRequest("SKU001", "Laptop", "Powerful laptop", new BigDecimal("1200.00"), categoryId);
        when(productRepository.findBySku(request.sku())).thenReturn(Optional.of(new Product()));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.create(request));
        assertEquals("Product with SKU " + request.sku() + " already exists.", exception.getMessage());
        verify(productRepository, times(1)).findBySku(request.sku());
        verify(categoryRepository, never()).findById(anyLong());
        verify(productMapper, never()).toEntity(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProduct_shouldThrowRuntimeException_whenCategoryDoesNotExist() {
        // Given
        Long categoryId = 1L;
        CreateProductRequest request = new CreateProductRequest("SKU001", "Laptop", "Powerful laptop", new BigDecimal("1200.00"), categoryId);
        when(productRepository.findBySku(request.sku())).thenReturn(Optional.empty());
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.create(request));
        assertEquals("Category not found with id: " + categoryId, exception.getMessage());
        verify(productRepository, times(1)).findBySku(request.sku());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productMapper, never()).toEntity(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void getProduct_shouldReturnProductResponse_whenProductExists() {
        // Given
        Long productId = 1L;
        Long categoryId = 1L;
        Category category = Category.builder().id(categoryId).name("Electronics").build();
        Product productEntity = Product.builder()
                .id(productId).sku("SKU001").name("Laptop").price(new BigDecimal("1200.00")).category(category).active(true)
                .build();
        ProductResponse expectedResponse = new ProductResponse(productId, "SKU001", "Laptop", null, new BigDecimal("1200.00"), true, categoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productMapper.toResponse(productEntity)).thenReturn(expectedResponse);

        // When
        ProductResponse actualResponse = productService.get(productId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).toResponse(productEntity);
    }

    @Test
    void getProduct_shouldThrowRuntimeException_whenProductDoesNotExist() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.get(productId));
        assertEquals("Product not found with id: " + productId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, never()).toResponse(any());
    }

    @Test
    void updateProduct_shouldReturnUpdatedProductResponse_whenProductAndCategoryExistAndSkuIsUnique() {
        // Given
        Long productId = 1L;
        Long oldCategoryId = 1L;
        Long newCategoryId = 2L;
        UpdateProductRequest request = new UpdateProductRequest("SKU002", "Desktop", "Powerful desktop", new BigDecimal("1500.00"), newCategoryId);
        
        Category oldCategory = Category.builder().id(oldCategoryId).name("Electronics").build();
        Category newCategory = Category.builder().id(newCategoryId).name("Computers").build();

        Product existingProduct = Product.builder()
                .id(productId).sku("SKU001").name("Laptop").description("Powerful laptop").price(new BigDecimal("1200.00")).category(oldCategory).active(true)
                .build();
        Product updatedProduct = Product.builder()
                .id(productId).sku("SKU002").name("Desktop").description("Powerful desktop").price(new BigDecimal("1500.00")).category(newCategory).active(true)
                .build();
        ProductResponse expectedResponse = new ProductResponse(productId, "SKU002", "Desktop", "Powerful desktop", new BigDecimal("1500.00"), true, newCategoryId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.findBySku(request.sku())).thenReturn(Optional.empty()); // New SKU is unique
        when(categoryRepository.findById(newCategoryId)).thenReturn(Optional.of(newCategory));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toResponse(updatedProduct)).thenReturn(expectedResponse);

        // When
        ProductResponse actualResponse = productService.update(productId, request);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.sku(), actualResponse.sku());
        assertEquals(expectedResponse.categoryId(), actualResponse.categoryId());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findBySku(request.sku());
        verify(categoryRepository, times(1)).findById(newCategoryId);
        verify(productRepository, times(1)).save(existingProduct);
        verify(productMapper, times(1)).toResponse(updatedProduct);
    }

    @Test
    void updateProduct_shouldThrowRuntimeException_whenProductDoesNotExist() {
        // Given
        Long productId = 1L;
        Long categoryId = 1L;
        UpdateProductRequest request = new UpdateProductRequest("SKU002", "Desktop", "Powerful desktop", new BigDecimal("1500.00"), categoryId);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.update(productId, request));
        assertEquals("Product not found with id: " + productId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).findBySku(anyString());
        verify(categoryRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_shouldThrowRuntimeException_whenNewSkuAlreadyExists() {
        // Given
        Long productId = 1L;
        Long categoryId = 1L;
        UpdateProductRequest request = new UpdateProductRequest("SKU002", "Desktop", "Powerful desktop", new BigDecimal("1500.00"), categoryId);
        
        Category category = Category.builder().id(categoryId).name("Electronics").build();
        Product existingProduct = Product.builder()
                .id(productId).sku("SKU001").name("Laptop").description("Powerful laptop").price(new BigDecimal("1200.00")).category(category).active(true)
                .build();
        Product anotherProductWithSameSku = Product.builder().id(2L).sku("SKU002").build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.findBySku(request.sku())).thenReturn(Optional.of(anotherProductWithSameSku));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.update(productId, request));
        assertEquals("Product with SKU " + request.sku() + " already exists.", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).findBySku(request.sku());
        verify(categoryRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_shouldThrowRuntimeException_whenNewCategoryDoesNotExist() {
        // Given
        Long productId = 1L;
        Long oldCategoryId = 1L;
        Long newCategoryId = 2L;
        UpdateProductRequest request = new UpdateProductRequest("SKU001", "Desktop", "Powerful desktop", new BigDecimal("1500.00"), newCategoryId); // SKU is same, so no SKU check
        
        Category oldCategory = Category.builder().id(oldCategoryId).name("Electronics").build();
        Product existingProduct = Product.builder()
                .id(productId).sku("SKU001").name("Laptop").description("Powerful laptop").price(new BigDecimal("1200.00")).category(oldCategory).active(true)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        // Note: the service doesn't call findBySku if SKU is same.
        // when(productRepository.findBySku(request.sku())).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(newCategoryId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.update(productId, request));
        assertEquals("Category not found with id: " + newCategoryId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        // verify(productRepository, times(1)).findBySku(request.sku());
        verify(categoryRepository, times(1)).findById(newCategoryId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void getAllProducts_shouldReturnListOfProductResponses() {
        // Given
        Long categoryId = 1L;
        Category category = Category.builder().id(categoryId).name("Electronics").build();
        Product product1 = Product.builder().id(1L).sku("SKU001").name("Laptop").price(new BigDecimal("1200.00")).category(category).active(true).build();
        Product product2 = Product.builder().id(2L).sku("SKU002").name("Mouse").price(new BigDecimal("25.00")).category(category).active(true).build();
        List<Product> productEntities = Arrays.asList(product1, product2);

        ProductResponse response1 = new ProductResponse(1L, "SKU001", "Laptop", null, new BigDecimal("1200.00"), true, categoryId);
        ProductResponse response2 = new ProductResponse(2L, "SKU002", "Mouse", null, new BigDecimal("25.00"), true, categoryId);
        List<ProductResponse> expectedResponses = Arrays.asList(response1, response2);

        when(productRepository.findAll()).thenReturn(productEntities);
        when(productMapper.toResponse(product1)).thenReturn(response1);
        when(productMapper.toResponse(product2)).thenReturn(response2);

        // When
        List<ProductResponse> actualResponses = productService.getAll();

        // Then
        assertNotNull(actualResponses);
        assertEquals(2, actualResponses.size());
        assertEquals(expectedResponses.get(0).id(), actualResponses.get(0).id());
        assertEquals(expectedResponses.get(1).id(), actualResponses.get(1).id());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(2)).toResponse(any(Product.class));
    }

    @Test
    void deleteProduct_shouldSetProductToInactive_whenProductExists() {
        // Given
        Long productId = 1L;
        Product existingProduct = Product.builder().id(productId).sku("SKU001").name("Laptop").active(true).build();
        Product inactiveProduct = Product.builder().id(productId).sku("SKU001").name("Laptop").active(false).build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(inactiveProduct);

        // When
        productService.delete(productId);

        // Then
        assertFalse(existingProduct.getActive()); // Verify that the active status was changed
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void deleteProduct_shouldThrowRuntimeException_whenProductDoesNotExist() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.delete(productId));
        assertEquals("Product not found with id: " + productId, exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }
}
