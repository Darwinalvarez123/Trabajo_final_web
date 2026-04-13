package com.tienda.unimagTienda.api.controller;

import com.tienda.unimagTienda.api.dto.AddressDto.*;
import com.tienda.unimagTienda.domine.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> create(@PathVariable Long customerId, @Valid @RequestBody CreateAddressRequest req) {
        return new ResponseEntity<>(addressService.create(req), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAllByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAllByCustomerId(customerId));
    }
}
