package com.tienda.unimagTienda.api.dto;

import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import java.io.Serializable;

public class CustomerDto {
    public record CustomerCreateRequest(
            String firstName,
            String lastName,
            String email,
            String phone,
            CustomerStatus status) implements Serializable {}

    public record CustomerResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone,
            CustomerStatus status) implements Serializable {}
}
