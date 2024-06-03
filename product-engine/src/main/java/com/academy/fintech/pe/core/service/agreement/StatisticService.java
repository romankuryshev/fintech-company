package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementStatus;
import com.academy.fintech.pe.core.service.agreement.db.loan_payment.LoanPaymentStatus;
import com.academy.fintech.pe.core.service.agreement.db.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.exception.ProductDoesNotExists;
import com.academy.fintech.pe.grpc.service.agreement.statistic.dto.ClientAgreementDto;
import com.academy.fintech.scoring.application_processing.AgreementResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticService {

    private final ProductService productService;
    private final PaymentScheduleService paymentScheduleService;

    private final AgreementService agreementService;

    public Product getProduct(String productCode) {
        Product product = productService.getProduct(productCode);
        if (product == null) {
            throw new ProductDoesNotExists("Product with code - " + productCode + " doesn't exists.");
        }
        return product;
    }

    public ClientAgreementDto getClientAgreements(UUID clientId) {
        List<AgreementResponse> agreements = agreementService.getAllAgreementsByClientId(clientId).stream()
                .filter(agreement -> !agreement.getStatus().equals(AgreementStatus.NEW))
                .map(agreement -> AgreementResponse.newBuilder()
                        .setAgreementId(agreement.getId().toString())
                        .setCountExpiredPayments(getCountOverduePayments(agreement))
                        .build())
                .collect(Collectors.toList());

        return new ClientAgreementDto(agreements);
    }

    private int getCountOverduePayments(Agreement agreement) {
        return (int) paymentScheduleService.getActualSchedule(agreement)
                .getPayments().stream()
                .filter(payment -> payment.getStatus().equals(LoanPaymentStatus.OVERDUE))
                .count();
    }
}
