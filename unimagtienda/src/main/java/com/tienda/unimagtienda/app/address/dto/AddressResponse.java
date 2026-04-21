package com.tienda.unimagtienda.app.address.dto;

public record AddressResponse(

        long id,
        String street,
        String city,
        String state,
        String postalCode,
        String country,
        Long customerId

) {}