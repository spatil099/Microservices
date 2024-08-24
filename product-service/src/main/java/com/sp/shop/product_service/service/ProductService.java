package com.sp.shop.product_service.service;

import com.sp.shop.product_service.dto.ProductRequest;
import com.sp.shop.product_service.dto.ProductResponse;
import com.sp.shop.product_service.model.Product;
import com.sp.shop.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    public ProductResponse create(ProductRequest productRequest) {
        Product product = Product.builder().id(productRequest.id())
                .description(productRequest.description())
                .name(productRequest.name())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product id : {} is create successfully", productRequest.id());
        return new ProductResponse(product.getName(), product.getDescription(), product.getId(), product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> new ProductResponse(product.getName(), product.getDescription(), product.getId(), product.getPrice())).toList();
    }
}
