package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.AddressDto.*;
import com.tienda.unimagTienda.domine.entity.Address;
import com.tienda.unimagTienda.domine.repository.AddressRepository;
import com.tienda.unimagTienda.domine.repository.CustomerRepository;
import com.tienda.unimagTienda.domine.service.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository; // Needed to validate customer existence
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressResponse create(AddressCreateRequest req) {
        // Validate if customer exists
        customerRepository.findById(req.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + req.customerId()));

        Address address = addressMapper.toEntity(req);
        address = addressRepository.save(address);
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse get(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse update(Long id, AddressCreateRequest req) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        // Validate if customer exists if customerId is changed
        if (!address.getCustomer().getId().equals(req.customerId())) {
            customerRepository.findById(req.customerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + req.customerId()));
        }
        
        address.setStreet(req.street());
        address.setCity(req.city());
        address.setState(req.state());
        address.setPostalCode(req.postalCode());
        address.setCountry(req.country());
        address.getCustomer().setId(req.customerId()); // Update customer reference
        
        address = addressRepository.save(address);
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAllByCustomerId(Long customerId) {
        // Validate if customer exists
        customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        return addressRepository.findByCustomerId(customerId).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
    }
}
