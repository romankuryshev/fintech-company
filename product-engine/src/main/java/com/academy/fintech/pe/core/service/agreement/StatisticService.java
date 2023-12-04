package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.exception.ProductDoesNotExists;
import com.academy.fintech.pe.grpc.service.agreement.statistic.dto.ClientAgreementDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StatisticService {

    private final ProductService productService;

    private final AgreementService agreementService;

    public Product getProduct(String productCode) {
        Product product = productService.getProduct(productCode);
        if (product == null) {
            throw new ProductDoesNotExists("Product with code - " + productCode + " doesn't exists.");
        }
        return product;
    }

    public ClientAgreementDto getClientAgreements(UUID clientId) {
        List<Agreement> agreements = agreementService.getAllAgreementsByClientId(clientId);
        return new ClientAgreementDto(agreements);
    }
}
