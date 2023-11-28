package com.academy.fintech.pe.core.service.agreement.db.product;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(String productCode) {
        return productRepository.findByCode(productCode);
    }
}
