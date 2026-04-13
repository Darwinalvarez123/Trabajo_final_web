package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.CustomerDto.*;
import com.tienda.unimagTienda.domine.entity.Customer;
import com.tienda.unimagTienda.domine.repository.CustomerRepository;
import com.tienda.unimagTienda.domine.service.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse create(CreateCustomerRequest req) {
        Customer customer = customerMapper.toEntity(req);
        customer = customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse get(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, UpdateCustomerRequest req) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        
        customer.setFirstName(req.firstName());
        customer.setLastName(req.lastName());
        customer.setEmail(req.email());
        customer.setPhone(req.phone());
        customer.setStatus(req.status());
        
        customer = customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
