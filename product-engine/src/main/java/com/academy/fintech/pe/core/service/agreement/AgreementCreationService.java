package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.account.db.AccountService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.exception.InvalidAgreementParametersException;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgreementCreationService {

    private final AgreementService agreementService;

    private final ProductService productService;

    private final AccountService accountService;

    @Transactional
    public Agreement createAgreement(AgreementDto agreementDto) {
        Product product = productService.getProduct(agreementDto.productCode());

        List<String> validationErrors = validateAgreementDto(agreementDto, product);
        if (!validateAgreementDto(agreementDto, product).isEmpty()) {
            log.error("Agreement validation failed. Errors: " + validationErrors);
            throw new InvalidAgreementParametersException(validationErrors);
        }

        Agreement agreement = agreementService.create(agreementDto, product);
        accountService.create(agreement.getId());
        return agreement;
    }

    private List<String> validateAgreementDto(AgreementDto agreementDto, Product product) {

        List<String> validationErrors = new ArrayList<>();

        if (product == null) {
            validationErrors.add("Product with code '" + agreementDto.productCode() + "' doesn't exists");
            return validationErrors;
        }

        int term = agreementDto.term();
        if (term < product.getMinTerm() || term > product.getMaxTerm()) {
            validationErrors.add("Invalid agreements term.");
        }

        BigDecimal interest = agreementDto.interest();
        if (interest.compareTo(product.getMinInterest()) < 0 || interest.compareTo(product.getMaxInterest()) > 0) {
            validationErrors.add("Invalid agreements interest.");
        }

        BigDecimal principalAmount = agreementDto.principalAmount();
        if (principalAmount.compareTo(product.getMinPrincipalAmount()) < 0 || principalAmount.compareTo(product.getMaxPrincipalAmount()) > 0) {
            validationErrors.add("Invalid agreements principalAmount.");
        }

        BigDecimal originationAmount = agreementDto.originationAmount();
        if (originationAmount.compareTo(product.getMinOriginationAmount()) < 0 || originationAmount.compareTo(product.getMaxOriginationAmount()) > 0) {
            validationErrors.add("Invalid agreements originationAmount.");
        }

        return validationErrors;
    }
}
