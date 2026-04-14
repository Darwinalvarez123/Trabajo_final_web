package com.tienda.unimagTienda.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.unimagTienda.api.dto.InventoryDto.*;
import com.tienda.unimagTienda.api.dto.ProductDto.*;
import com.tienda.unimagTienda.domine.service.InventoryService;
import com.tienda.unimagTienda.domine.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/products - Debe crear un producto exitosamente")
    void createProduct_Success() throws Exception {
        CreateProductRequest request = new CreateProductRequest("SKU-001", "Libro Java", "Descripción", new BigDecimal("50.00"), 1L);
        ProductResponse response = new ProductResponse(1L, "SKU-001", "Libro Java", "Descripción", new BigDecimal("50.00"), true, 1L);

        when(productService.create(any(CreateProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.name").value("Libro Java"));
    }

    @Test
    @DisplayName("PUT /api/products/{id}/inventory - Debe actualizar el inventario de un producto")
    void updateInventory_Success() throws Exception {
        UpdateInventoryRequest request = new UpdateInventoryRequest(1L, 100, 10);
        InventoryResponse response = new InventoryResponse(1L, 1L, "Libro Java", 100, 10, null);

        when(inventoryService.getByProductId(1L)).thenReturn(response);
        when(inventoryService.update(eq(1L), any(InventoryRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/products/1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableStock").value(100));
    }
}
