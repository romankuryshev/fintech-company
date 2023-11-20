package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AgreementCreationService {

    private final AgreementService agreementService;

    private final ProductService productService;

    @Autowired
    public AgreementCreationService(AgreementService agreementService, ProductService productService) {
        this.agreementService = agreementService;
        this.productService = productService;
    }

    public Optional<Agreement> createAgreement(AgreementDto agreementDto) {
        Product product = productService.getProduct(agreementDto.productCode());
        if(product == null || !isValid(agreementDto, product)) {
            return Optional.empty();
        }
        return Optional.of(agreementService.create(agreementDto, product));
    }

    private boolean isValid(AgreementDto agreementDto, Product product) {

        Integer term = agreementDto.term();
        if (term.compareTo(product.getMinTerm()) < 0 || term.compareTo(product.getMaxTerm()) > 0) {
            return false;
        }

        BigDecimal interest = agreementDto.interest();
        if (interest.compareTo(product.getMinInterest()) < 0 || interest.compareTo(product.getMaxInterest()) > 0) {
            return false;
        }

        BigDecimal principalAmount = agreementDto.principalAmount();
        if (principalAmount.compareTo(product.getMinPrincipalAmount()) < 0 || principalAmount.compareTo(product.getMaxPrincipalAmount()) > 0) {
            return false;
        }

        BigDecimal originationAmount = agreementDto.originationAmount();
        if (originationAmount.compareTo(product.getMinOriginationAmount()) < 0 || originationAmount.compareTo(product.getMaxOriginationAmount()) > 0) {
            return false;
        }

        return true;
    }
}
