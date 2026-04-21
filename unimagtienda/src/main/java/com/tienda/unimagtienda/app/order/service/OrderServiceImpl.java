package com.tienda.unimagtienda.app.order.service;

import com.tienda.unimagtienda.app.address.entity.Address;
import com.tienda.unimagtienda.app.address.repository.AddressRepository;
import com.tienda.unimagtienda.app.customer.entity.Customer;
import com.tienda.unimagtienda.app.customer.enums.CustomerStatus;
import com.tienda.unimagtienda.app.customer.repository.CustomerRepository;
import com.tienda.unimagtienda.app.inventory.entity.Inventory;
import com.tienda.unimagtienda.app.inventory.repository.InventoryRepository;
import com.tienda.unimagtienda.app.order.dto.CreateOrderRequest;
import com.tienda.unimagtienda.app.order.dto.OrderResponse;
import com.tienda.unimagtienda.app.order.entity.Order;
import com.tienda.unimagtienda.app.order.enums.OrderStatus;
import com.tienda.unimagtienda.app.order.mapper.OrderMapper;
import com.tienda.unimagtienda.app.order.repository.OrderRepository;
import com.tienda.unimagtienda.app.orderItem.entity.OrderItem;
import com.tienda.unimagtienda.app.orderStatusHistory.dto.OrderStatusHistoryResponse;
import com.tienda.unimagtienda.app.orderStatusHistory.entity.OrderStatusHistory;
import com.tienda.unimagtienda.app.orderStatusHistory.mapper.OrderStatusHistoryMapper;
import com.tienda.unimagtienda.app.orderStatusHistory.repository.OrderStatusHistoryRepository;
import com.tienda.unimagtienda.app.orderStatusHistory.service.OrderStatusHistoryService;
import com.tienda.unimagtienda.app.product.entity.Product;
import com.tienda.unimagtienda.app.product.repository.ProductRepository;
import com.tienda.unimagtienda.exception.BusinessException;
import com.tienda.unimagtienda.exception.ResourceNotFoundException;
import com.tienda.unimagtienda.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderMapper orderMapper;
    private final OrderStatusHistoryService orderStatusHistoryService;

    // 🔥 HISTORIAL (FALTABAN ESTOS)
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Override
    public OrderResponse createOrder(CreateOrderRequest req) {

        Customer customer = customerRepository.findById(req.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer does not exist"));

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Customer is inactive");
        }

        Address address = addressRepository.findById(req.addressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address does not exist"));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new BusinessException("The address does not belong to the customer");
        }

        if (req.items() == null || req.items().isEmpty()) {
            throw new ValidationException("The order must have at least one item");
        }

        Order order = orderMapper.toEntity(req);
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (var itemReq : req.items()) {

            if (itemReq.quantity() <= 0) {
                throw new ValidationException("Invalid quantity");
            }

            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product does not exist"));

            if (!product.getActive()) {
                throw new BusinessException("Product is inactive");
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemReq.quantity()));

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            items.add(item);
            total = total.add(subtotal);
        }

        order.setItems(items);
        order.setTotal(total);

        Order saved = orderRepository.save(order);

        orderStatusHistoryService.register(
                saved,
                OrderStatus.CREATED,
                "Order created"
        );

        return orderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order does not exist"));
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public void payOrder(Long id) {

        Order order = getOrderOrThrow(id);

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BusinessException("Only CREATED orders can be paid");
        }

        for (OrderItem item : order.getItems()) {
            Inventory inv = inventoryRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory does not exist"));

            if (inv.getAvailableStock() < item.getQuantity()) {
                throw new BusinessException("Insufficient stock");
            }
        }

        for (OrderItem item : order.getItems()) {
            Inventory inv = inventoryRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory does not exist"));

            inv.setAvailableStock(inv.getAvailableStock() - item.getQuantity());
            inventoryRepository.save(inv);
        }

        order.setStatus(OrderStatus.PAID);
        Order saved = orderRepository.save(order);

        orderStatusHistoryService.register(saved, OrderStatus.PAID, "Payment confirmed");
    }

    @Override
    public void shipOrder(Long id) {

        Order order = getOrderOrThrow(id);

        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessException("Only PAID orders can be shipped");
        }

        order.setStatus(OrderStatus.SHIPPED);
        Order saved = orderRepository.save(order);

        orderStatusHistoryService.register(saved, OrderStatus.SHIPPED, "Order shipped");
    }

    @Override
    public void deliverOrder(Long id) {

        Order order = getOrderOrThrow(id);

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new BusinessException("Only SHIPPED orders can be delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);
        Order saved = orderRepository.save(order);

        orderStatusHistoryService.register(saved, OrderStatus.DELIVERED, "Order delivered");
    }

    @Override
    public void cancelOrder(Long id) {

        Order order = getOrderOrThrow(id);

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessException("Delivered orders cannot be cancelled");
        }

        if (order.getStatus() == OrderStatus.PAID) {
            for (OrderItem item : order.getItems()) {
                Inventory inv = inventoryRepository.findByProductId(item.getProduct().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Inventory does not exist"));

                inv.setAvailableStock(inv.getAvailableStock() + item.getQuantity());
                inventoryRepository.save(inv);
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);

        orderStatusHistoryService.register(saved, OrderStatus.CANCELLED, "Order cancelled");
    }

    // =========================
    // HISTORIAL (NUEVO)
    // =========================
    @Override
    public List<OrderStatusHistoryResponse> getOrderHistory(Long orderId) {

        Order order = getOrderOrThrow(orderId);

        return orderStatusHistoryRepository
                .findByOrderIdOrderByCreatedAtAsc(orderId)
                .stream()
                .map(orderStatusHistoryMapper::toResponse)
                .toList();
    }

    private Order getOrderOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order does not exist"));
    }
}