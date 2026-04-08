package com.tienda.unimagTienda.domine.service;

import com.tienda.unimagTienda.api.dto.InventoryDto.UpdateStockRequest;
import com.tienda.unimagTienda.api.dto.OrderDto.*;
import com.tienda.unimagTienda.api.dto.OrderItemDto.*;
import com.tienda.unimagTienda.domine.entity.*;
import com.tienda.unimagTienda.domine.enums.CustomerStatus;
import com.tienda.unimagTienda.domine.enums.OrderStatus;
import com.tienda.unimagTienda.domine.repository.*;
import com.tienda.unimagTienda.domine.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final OrderStatusHistoryService orderStatusHistoryService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse create(OrderCreateRequest req) {
        // 6.2 Regla: El cliente debe existir y estar activo
        Customer customer = customerRepository.findById(req.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new RuntimeException("Inactive customers cannot create orders");
        }

        // 6.2 Regla: Dirección de envío debe pertenecer al cliente
        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Shipping address does not belong to the selected customer");
        }

        // 6.2 Regla: Al menos un ítem
        if (req.items() == null || req.items().isEmpty()) {
            throw new RuntimeException("Order must have at least one item");
        }

        Order order = Order.builder()
                .customer(customer)
                .shippingAddress(address)
                .status(OrderStatus.CREATED) // 6.2 Regla: Estado inicial CREATED
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemCreateRequest itemReq : req.items()) {
            // 6.2 Regla: Cantidades > 0
            if (itemReq.quantity() <= 0) {
                throw new RuntimeException("Item quantity must be greater than zero");
            }

            // 6.2 Regla: El producto debe existir y estar activo
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.productId()));
            
            if (!product.getActive()) {
                throw new RuntimeException("Product is not active: " + product.getName());
            }

            // 6.2 Regla: Subtotal e ítem
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .unitPrice(product.getPrice()) // Precio capturado al crear el pedido
                    .subtotal(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .build();

            order.getItems().add(item);
            total = total.add(item.getSubtotal());
        }

        order.setTotal(total); // 6.2 Regla: Total se calcula automáticamente
        order = orderRepository.save(order);

        orderStatusHistoryService.addHistory(order.getId(), "Order created successfully");

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse pay(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // 6.3 Regla: Solo de CREATED a PAID
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Only orders in CREATED status can be paid");
        }

        // 6.3 Regla: Validar y descontar stock antes de pasar a PAID
        for (OrderItem item : order.getItems()) {
            inventoryService.updateStock(new UpdateStockRequest(item.getProduct().getId(), -item.getQuantity()));
        }

        order.setStatus(OrderStatus.PAID);
        order = orderRepository.save(order);

        orderStatusHistoryService.addHistory(order.getId(), "Order marked as PAID and stock deducted");

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse ship(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // 6.4 Regla: Solo de PAID a SHIPPED
        if (order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Only PAID orders can be shipped");
        }

        order.setStatus(OrderStatus.SHIPPED);
        order = orderRepository.save(order);

        orderStatusHistoryService.addHistory(order.getId(), "Order marked as SHIPPED");

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse deliver(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // 6.4 Regla: Solo de SHIPPED a DELIVERED
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("Only SHIPPED orders can be delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);
        order = orderRepository.save(order);

        orderStatusHistoryService.addHistory(order.getId(), "Order marked as DELIVERED");

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // 6.5 Regla: Un pedido DELIVERED no puede cancelarse.
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered orders cannot be cancelled");
        }
        
        // 6.5 Regla: Un pedido SHIPPED no se puede cancelar si es la regla.
        if (order.getStatus() == OrderStatus.SHIPPED) {
             throw new RuntimeException("Shipped orders cannot be cancelled");
        }

        // 6.5 Regla: Reversión de stock si ya estaba pagado.
        if (order.getStatus() == OrderStatus.PAID) {
            for (OrderItem item : order.getItems()) {
                inventoryService.updateStock(new UpdateStockRequest(item.getProduct().getId(), item.getQuantity()));
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        orderStatusHistoryService.addHistory(order.getId(), "Order has been CANCELLED");
        
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }
}
