package com.academy.fintech.scoring.core.processing;

import com.academy.fintech.scoring.core.pe.client.ProductEngineClientService;
import com.academy.fintech.scoring.core.processing.exception.ProductDoesNotExists;
import com.academy.fintech.scoring.core.processing.dto.AgreementDto;
import com.academy.fintech.scoring.core.processing.model.Product;
import com.academy.fintech.scoring.public_interface.processing.dto.ProcessApplicationRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Сервис вычисляет оценку для клиента, на основе которой принимает решение об одобрении либо
 * отказе в выдаче кредита.
 * Значения {@code PRODUCT_CODE, INTEREST, TERM_IN_MONTHS} в дальнейшем будут поступать через {@code ProcessApplicationRequestDto}.
 */
@Service
@Slf4j
@AllArgsConstructor
public class ScoringService {

    private static final int INCREMENTAL_STEP = 5;
    private static final BigDecimal PART_OF_SALARY = BigDecimal.valueOf(3);

    private final ProductEngineClientService productEngineClientService;

    public ProcessingResult process(ProcessApplicationRequestDto requestDto) {
        Product product;
        try {
            product = productEngineClientService.getProduct(requestDto.productCode());
        } catch (ProductDoesNotExists e) {
            return ProcessingResult.CANCELED;
        }

        List<AgreementDto> agreements = productEngineClientService.getClientAgreements(requestDto.clientId());
        BigDecimal paymentAmount = productEngineClientService.getPaymentAmount(requestDto.interest(), requestDto.termInMonths(), requestDto.disbursementAmount());

        if (!isValid(product, requestDto.disbursementAmount(), requestDto.termInMonths(), requestDto.interest())) {
            return ProcessingResult.CANCELED;
        }

        int clientScore = calculateClientScore(agreements, paymentAmount, requestDto);
        if (clientScore <= 0) {
            return ProcessingResult.CANCELED;
        }
        return ProcessingResult.ACCEPTED;
    }

    /**
     * Метод рассчитывает оценку клиента исходя из его кредитной истории.
     * Наличие каждого просроченного более чем на {@code PERMITTED_DELAY_IN_DAYS} дней кредита уменьшает оценку на 1.
     * Аналогично, наличие кредитов оплаченных в срок, высчитывается как увеличивает оценку на 1.
     *
     * @param agreements  договора клиента
     * @param paymentAmount  сумма ежемесячного платежа, при одобрении кредита
     * @param clientSalary  заработная плата клиента
     * @return оценка клиента.
     */
    private int calculateClientScore(List<AgreementDto> agreements, BigDecimal paymentAmount, ProcessApplicationRequestDto requestDto) {
        int clientScore = 0;

        if (requestDto.clientSalary().divide(PART_OF_SALARY, RoundingMode.HALF_UP).compareTo(paymentAmount) > 0) {
            clientScore++;
        }

        if (agreements.isEmpty()) {
            clientScore++;
        }

        for (var agreement : agreements) {
            if (agreement.countExpiredPayments() == 0) {
                clientScore++;
            } else {
                clientScore -= agreement.countExpiredPayments() % INCREMENTAL_STEP;
            }
        }

        return clientScore;
    }

    private boolean isValid(Product product, BigDecimal disbursementAmount, int termInMonths, BigDecimal interest) {

        if (termInMonths < product.minTermInMonths() || termInMonths > product.maxTermInMonths()) {
            return false;
        }

        if (interest.compareTo(product.minInterest()) < 0 || interest.compareTo(product.maxInterest()) > 0) {
            return false;
        }

        if (disbursementAmount.compareTo(product.minPrincipalAmount()) < 0 || disbursementAmount.compareTo(product.maxPrincipalAmount()) > 0) {
            return false;
        }

        return true;
    }
}
