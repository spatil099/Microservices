package com.sp.shop.product_service.dto;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public record ProductResponse(String name,

        String description,
        String id,

        BigDecimal price) {
}
