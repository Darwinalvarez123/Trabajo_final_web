package com.tienda.unimagtienda.app.customer.service;

import com.tienda.unimagtienda.app.customer.dto.CreateCustomerRequest;
import com.tienda.unimagtienda.app.customer.dto.CustomerResponse;
import com.tienda.unimagtienda.app.customer.dto.UpdateCustomerRequest;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.enums.CustomerStatus;
import com.tienda.unimagtienda.app.customer.mapper.CustomerMapper;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.exception.ConflictException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public CustomerResponse create(CreateCustomerRequest req) {

        if (repository.existsByEmail(req.email())) {
            throw new ConflictException("Email already exists");
        }

        Customer customer = mapper.toEntity(req);
        Customer saved = repository.save(customer);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getById(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));

        if (customer.getStatus() == CustomerStatus.INACTIVE) {
            throw new ResourceNotFoundException("Customer inactive: " + id);
        }

        return mapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAll() {

        return repository.findAll()
                .stream()
                .filter(c -> c.getStatus() == CustomerStatus.ACTIVE)
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse update(Long id, UpdateCustomerRequest req) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));

        if (customer.getStatus() == CustomerStatus.INACTIVE) {
            throw new ResourceNotFoundException("Customer inactive: " + id);
        }

        if (req.email() != null &&
                !req.email().equals(customer.getEmail()) &&
                repository.existsByEmail(req.email())) {

            throw new ConflictException("Email already exists");
        }

        mapper.update(customer, req);

        Customer updated = repository.save(customer);

        return mapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));

        customer.setStatus(CustomerStatus.INACTIVE);
        repository.save(customer);
    }
}