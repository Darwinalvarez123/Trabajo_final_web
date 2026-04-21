package com.tienda.unimagtienda.app.inventory.dto;

import java.io.Serializable;

public record UpdateInventoryRequest(

        Integer availableStock,
        Integer minStock

) implements Serializable {}