package com.tienda.unimagTienda.api.dto;

import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class CustomerDto {
    public record CreateCustomerRequest(
            @NotBlank(message = "First name is mandatory") String firstName,
            @NotBlank(message = "Last name is mandatory") String lastName,
            @NotBlank(message = "Email is mandatory") @Email(message = "Invalid email format") String email,
            String phone,
            @NotNull(message = "Status is mandatory") CustomerStatus status) implements Serializable {}

    public record UpdateCustomerRequest(
            @NotBlank(message = "First name is mandatory") String firstName,
            @NotBlank(message = "Last name is mandatory") String lastName,
            @NotBlank(message = "Email is mandatory") @Email(message = "Invalid email format") String email,
            String phone,
            @NotNull(message = "Status is mandatory") CustomerStatus status) implements Serializable {}

    public record CustomerResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone,
            CustomerStatus status) implements Serializable {}
}
