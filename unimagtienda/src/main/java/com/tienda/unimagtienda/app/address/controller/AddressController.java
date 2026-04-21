package com.tienda.unimagtienda.app.address.controller;

import com.tienda.unimagtienda.app.address.dto.AddressResponse;
import com.tienda.unimagtienda.app.address.dto.CreateAddressRequest;
import com.tienda.unimagtienda.app.address.dto.UpdateAddressRequest;
import com.tienda.unimagtienda.app.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse create(@Valid @RequestBody CreateAddressRequest req) {
        return service.create(req);
    }


    @GetMapping("/{id}")
    public AddressResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }


    @GetMapping
    public List<AddressResponse> getAll() {
        return service.getAll();
    }


    @PutMapping("/{id}")
    public AddressResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAddressRequest req
    ) {
        return service.update(id, req);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}