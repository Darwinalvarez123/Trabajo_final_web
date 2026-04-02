package com.tienda.unimagTienda.enums;

import lombok.Getter;

@Getter
public enum CustomerStatus {
    ACTIVE("Active customer"),
    INACTIVE("Inactive customer");

    private final String description;

    CustomerStatus(String description) {
        this.description = description;
    }
}