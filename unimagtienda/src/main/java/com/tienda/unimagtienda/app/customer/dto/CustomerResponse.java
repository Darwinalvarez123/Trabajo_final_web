package com.tienda.unimagtienda.app.customer.dto;

import com.tienda.unimagtienda.app.customer.enums.CustomerStatus;

import java.io.Serializable;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        CustomerStatus status
) implements Serializable {}