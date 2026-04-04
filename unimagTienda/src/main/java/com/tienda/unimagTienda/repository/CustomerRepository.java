package com.tienda.unimagTienda.repository;

import com.tienda.unimagTienda.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);

    // Traer clientes con sus órdenes (filtradas)
    @Query("""
        SELECT DISTINCT c FROM Customer c
        JOIN FETCH c.orders o
        WHERE o.status IN ('PAID', 'SHIPPED', 'DELIVERED')
    """)
    List<Customer> findCustomersWithValidOrders();
}