package com.tienda.unimagtienda.app.address.service;

import com.tienda.unimagtienda.app.address.dto.AddressResponse;
import com.tienda.unimagtienda.app.address.dto.CreateAddressRequest;
import com.tienda.unimagtienda.app.address.dto.UpdateAddressRequest;
import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.address.mapper.AddressMapper;
import com.tienda.unimagtienda.app.address.repository.AddressRepository;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.exception.ResourceNotFoundException; // 🔥 IMPORTANTE
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImp implements AddressService {

    private final AddressRepository repo;
    private final AddressMapper mapper;
    private final CustomerRepository customerRepository;

    @Override
    public AddressResponse create(CreateAddressRequest req) {

        Customer customer = customerRepository.findById(req.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found: " + req.customerId()
                ));

        Address address = mapper.toEntity(req);
        address.setCustomer(customer);

        Address saved = repo.save(address);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getById(Long id) {
        Address address = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found: " + id
                ));

        return mapper.toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public AddressResponse update(Long id, UpdateAddressRequest req) {

        Address address = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found: " + id
                ));

        mapper.update(address, req);

        Address updated = repo.save(address);
        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Address address = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found: " + id
                ));

        repo.delete(address);
    }
}