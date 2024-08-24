package com.sp.shop.order_service.dto;

import java.math.BigDecimal;

public record OrderResponse(Long id,
                            String orderNumber, String skuCode,
                            BigDecimal price,
                            Integer quantity) {
}
