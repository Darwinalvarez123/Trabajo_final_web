package com.tienda.unimagTienda.domine.repository;

import com.tienda.unimagTienda.domine.entity.*;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.save(Category.builder().name("Electro").build());
        
        product1 = productRepository.save(Product.builder()
                .name("Laptop")
                .sku("LAP-001")
                .price(BigDecimal.valueOf(1000))
                .active(true)
                .category(category)
                .build());

        product2 = productRepository.save(Product.builder()
                .name("Mouse")
                .sku("MOU-001")
                .price(BigDecimal.valueOf(50))
                .active(true)
                .category(category)
                .build());

        Customer customer = customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@test.com")
                .status(CustomerStatus.ACTIVE)
                .build());

        Address address = addressRepository.save(Address.builder()
                .street("Main St")
                .city("Santa Marta")
                .state("Magdalena")
                .postalCode("470001")
                .country("Colombia")
                .customer(customer)
                .build());

        Order order = orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.PAID)
                .total(BigDecimal.valueOf(1050))
                .build());

        orderItemRepository.save(OrderItem.builder()
                .order(order)
                .product(product1)
                .quantity(1)
                .unitPrice(product1.getPrice())
                .subtotal(product1.getPrice())
                .build());

        orderItemRepository.save(OrderItem.builder()
                .order(order)
                .product(product2)
                .quantity(2)
                .unitPrice(product2.getPrice())
                .subtotal(product2.getPrice().multiply(BigDecimal.valueOf(2)))
                .build());
    }

    @Test
    @DisplayName("Debe obtener los productos más vendidos")
    void shouldFindBestSellingProducts() {
        List<Object[]> results = orderItemRepository.findBestSellingProducts();

        assertThat(results).isNotEmpty();
        // El primer resultado debe ser Mouse (vendió 2 unidades)
        assertThat(results.get(0)[1]).isEqualTo("Mouse");
        assertThat(results.get(0)[2]).isEqualTo(2L);
    }

    @Test
    @DisplayName("Debe obtener el volumen de ventas por categoría")
    void shouldFindTopCategoriesByVolume() {
        List<Object[]> results = orderItemRepository.findTopCategoriesByVolume();

        assertThat(results).isNotEmpty();
        assertThat(results.get(0)[1]).isEqualTo("Electro");
        assertThat(results.get(0)[2]).isEqualTo(3L); // 1 laptop + 2 mice
    }
}
