package com.sp.shop.order_service.service;

import com.sp.shop.event.OrderPlacedEvent;
import com.sp.shop.order_service.client.InventoryClient;
import com.sp.shop.order_service.dto.OrderRequest;
import com.sp.shop.order_service.model.Order;
import com.sp.shop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public void placeOrder(OrderRequest orderRequest){

        boolean isInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(isInStock){
            Order order = Order.builder()
                    .price(orderRequest.price())
                    .quantity(orderRequest.quantity())
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderRequest.skuCode())
                    .id(orderRequest.id())
                    .build();
            orderRepository.save(order);
            log.info("Order : {} created successfully", order.getId());

            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(order.getOrderNumber(), orderRequest.userDetails().email());
            log.info("Sending event to kafka");
            kafkaTemplate.send("order-placed", orderPlacedEvent);
            log.info("Msg sent to kafka topic sucessfully");

        }else{
         throw new RuntimeException("Product with skuCode "+orderRequest.skuCode()+" is not in stock.")  ;
        }

    }
}
