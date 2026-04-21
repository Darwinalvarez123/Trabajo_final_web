package com.tienda.unimagtienda.app.customer.dto;

import jakarta.validation.constraints.Email;

import java.io.Serializable;

public record UpdateCustomerRequest(

        String firstName,
        String lastName,

        @Email(message = "Email must be valid")
        String email

) implements Serializable {}