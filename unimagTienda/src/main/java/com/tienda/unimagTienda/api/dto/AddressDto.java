package com.tienda.unimagTienda.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class AddressDto {
    public record CreateAddressRequest(
            @NotBlank(message = "Street is mandatory") String street,
            @NotBlank(message = "City is mandatory") String city,
            @NotBlank(message = "State is mandatory") String state,
            @NotBlank(message = "Postal code is mandatory") String postalCode,
            @NotBlank(message = "Country is mandatory") String country,
            @NotNull(message = "Customer ID is mandatory") Long customerId) implements Serializable {}

    public record AddressResponse(
            Long id,
            String street,
            String city,
            String state,
            String postalCode,
            String country,
            Long customerId) implements Serializable {}
}
