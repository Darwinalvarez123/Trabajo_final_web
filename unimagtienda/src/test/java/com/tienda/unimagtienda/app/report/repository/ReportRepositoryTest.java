package com.tienda.unimagtienda.app.report.repository;

import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.address.repository.AddressRepository;
import com.tienda.unimagtienda.app.category.entity.Category;
import com.tienda.unimagtienda.app.category.repository.CategoryRepository;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.app.order.repository.OrderRepository;
import com.tienda.unimagtienda.app.orderItem.entity.OrderItem;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.app.product.repository.ProductRepository;
import com.tienda.unimagtienda.app.report.dto.BestSellingProductResponse;
import com.tienda.unimagtienda.app.report.dto.LowStockProductResponse;
import com.tienda.unimagtienda.app.report.dto.MonthlyIncomeResponse;
import com.tienda.unimagtienda.app.report.dto.TopCustomerResponse;
import com.tienda.unimagtienda.shared.AbstractRepositoryIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReportRepositoryTest extends AbstractRepositoryIT {

    @Autowired private ReportRepository reportRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private InventoryRepository inventoryRepository;

    @Test
    @DisplayName("Report: mejores productos vendidos")
    void shouldReturnBestSellingProducts() {

        // Given
        var category = categoryRepository.save(Category.builder().name("General").build());
        var product = productRepository.save(Product.builder()
                .sku("PROD-01")
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .category(category)
                .active(true)
                .build());

        var customer = customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("best-selling@report.com")
                .build());

        var address = addressRepository.save(Address.builder()
                .street("Street")
                .city("City")
                .state("State")
                .postalCode("12345")
                .country("Country")
                .customer(customer)
                .build());

        var order = Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.DELIVERED)
                .total(new BigDecimal("100.00"))
                .build();

        var item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(2)
                .unitPrice(new BigDecimal("50.00"))
                .subtotal(new BigDecimal("100.00"))
                .build();

        order.setItems(List.of(item));
        orderRepository.save(order);

        // When
        List<BestSellingProductResponse> results = reportRepository.bestSellingProducts();

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().getProductName()).isEqualTo("Test Product");
        assertThat(results.getFirst().getTotalSold()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Report: ingresos mensuales")
    void shouldReturnMonthlyIncome() {

        // Given
        var customer = customerRepository.save(Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("income@report.com")
                .build());

        var address = addressRepository.save(Address.builder()
                .street("Street")
                .city("City")
                .state("State")
                .postalCode("12345")
                .country("Country")
                .customer(customer)
                .build());

        orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.DELIVERED)
                .total(new BigDecimal("150.00"))
                .build());

        // When
        List<MonthlyIncomeResponse> results = reportRepository.monthlyIncome();

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().getTotalIncome()).isEqualByComparingTo("150.00");
    }

    @Test
    @DisplayName("Report: mejores clientes")
    void shouldReturnTopCustomers() {

        // Given
        var customer = customerRepository.save(Customer.builder()
                .firstName("Top")
                .lastName("Customer")
                .email("top@report.com")
                .build());

        var address = addressRepository.save(Address.builder()
                .street("Street")
                .city("City")
                .state("State")
                .postalCode("12345")
                .country("Country")
                .customer(customer)
                .build());

        orderRepository.save(Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.DELIVERED)
                .total(new BigDecimal("500.00"))
                .build());

        // When
        List<TopCustomerResponse> results = reportRepository.topCustomers();

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().getCustomerName()).isEqualTo("Top Customer");
        assertThat(results.getFirst().getTotalSpent()).isEqualByComparingTo("500.00");
    }

    @Test
    @DisplayName("Report: productos con bajo stock")
    void shouldReturnLowStockProducts() {

        // Given
        var category = categoryRepository.save(Category.builder().name("Hardware").build());
        var product = productRepository.save(Product.builder()
                .sku("LOW-01")
                .name("Low Stock Item")
                .price(new BigDecimal("10.00"))
                .category(category)
                .active(true)
                .build());

        inventoryRepository.save(Inventory.builder()
                .product(product)
                .availableStock(2)
                .minStock(10)
                .build());

        // When
        List<LowStockProductResponse> results = reportRepository.lowStockProducts();

        // Then
        assertThat(results).isNotEmpty();
        assertThat(results.getFirst().getProductName()).isEqualTo("Low Stock Item");
        assertThat(results.getFirst().getAvailableStock()).isEqualTo(2);
    }
}
