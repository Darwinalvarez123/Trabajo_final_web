package com.tienda.unimagTienda.api.dto;

import java.io.Serializable;

public class AddressDto {
    public record AddressCreateRequest(
            String street,
            String city,
            String state,
            String postalCode,
            String country,
            Long customerId) implements Serializable {}

    public record AddressResponse(
            Long id,
            String street,
            String city,
            String state,
            String postalCode,
            String country,
            Long customerId) implements Serializable {}
}
