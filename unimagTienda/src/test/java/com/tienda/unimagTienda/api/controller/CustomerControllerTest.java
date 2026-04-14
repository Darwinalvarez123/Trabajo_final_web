package com.tienda.unimagTienda.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.unimagTienda.api.dto.CustomerDto.CreateCustomerRequest;
import com.tienda.unimagTienda.api.dto.CustomerDto.CustomerResponse;
import com.tienda.unimagTienda.api.dto.CustomerDto.UpdateCustomerRequest;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import com.tienda.unimagTienda.domine.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/customers - Debe crear un cliente exitosamente")
    void createCustomer_Success() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("John", "Doe", "john@example.com", "123456789", CustomerStatus.ACTIVE);
        CustomerResponse response = new CustomerResponse(1L, "John", "Doe", "john@example.com", "123456789", CustomerStatus.ACTIVE);

        when(customerService.create(any(CreateCustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("POST /api/customers - Debe fallar por validación (email inválido)")
    void createCustomer_ValidationError() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("John", "Doe", "invalid-email", "123456789", CustomerStatus.ACTIVE);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/customers/{id} - Debe obtener un cliente por ID")
    void getCustomer_Success() throws Exception {
        CustomerResponse response = new CustomerResponse(1L, "John", "Doe", "john@example.com", "123456789", CustomerStatus.ACTIVE);

        when(customerService.get(1L)).thenReturn(response);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("PUT /api/customers/{id} - Debe actualizar un cliente")
    void updateCustomer_Success() throws Exception {
        UpdateCustomerRequest request = new UpdateCustomerRequest("John", "Updated", "john@example.com", "987654321", CustomerStatus.ACTIVE);
        CustomerResponse response = new CustomerResponse(1L, "John", "Updated", "john@example.com", "987654321", CustomerStatus.ACTIVE);

        when(customerService.update(eq(1L), any(UpdateCustomerRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Updated"));
    }
}