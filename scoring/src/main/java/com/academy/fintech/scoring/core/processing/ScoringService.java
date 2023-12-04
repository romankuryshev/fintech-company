package com.academy.fintech.scoring.core.processing;

import com.academy.fintech.scoring.core.pe.client.ProductEngineClientService;
import com.academy.fintech.scoring.core.processing.model.AgreementDto;
import com.academy.fintech.scoring.core.processing.model.ProcessingResult;
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

    private static final String PRODUCT_CODE = "CL1.0";

    private static final BigDecimal INTEREST = BigDecimal.valueOf(8);

    private static final int TERM_IN_MONTHS = 12;

    private static final int PERMITTED_DELAY_IN_DAYS = 7;

    private final ProductEngineClientService productEngineClientService;

    public ProcessingResult process(ProcessApplicationRequestDto requestDto) {
        Product product = productEngineClientService.getProductInfo(PRODUCT_CODE);
        List<AgreementDto> agreements = productEngineClientService.getClientStatistic(requestDto.clientId());
        BigDecimal paymentAmount = productEngineClientService.getPaymentAmount(INTEREST, TERM_IN_MONTHS, requestDto.disbursementAmount());

        if (!isValid(product, requestDto.disbursementAmount())) {
            return ProcessingResult.CANCELED;
        }

        int clientScore = calculateClientScore(agreements, paymentAmount, requestDto.clientSalary());
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
    private int calculateClientScore(List<AgreementDto> agreements, BigDecimal paymentAmount, BigDecimal clientSalary) {
        int clientScore = 0;

        if (clientSalary.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP).compareTo(paymentAmount) > 0) {
            clientScore += 1;
        }

        LocalDate currentDate = LocalDate.now();

        if (agreements.isEmpty()) {
            clientScore++;
        }

        for (var agreement : agreements) {
            if (agreement.nextPaymentDate().plusDays(PERMITTED_DELAY_IN_DAYS).isBefore(currentDate)) {
                clientScore--;
            } else if (agreement.nextPaymentDate().isAfter(currentDate)) {
                clientScore++;
            }
        }

        return clientScore;
    }

    private boolean isValid(Product product, BigDecimal disbursementAmount) {

        if (TERM_IN_MONTHS < product.minTermInMonths() || TERM_IN_MONTHS > product.maxTermInMonths()) {
            return false;
        }

        if (INTEREST.compareTo(product.minInterest()) < 0 || INTEREST.compareTo(product.maxInterest()) > 0) {
            return false;
        }

        if (disbursementAmount.compareTo(product.minPrincipalAmount()) < 0 || disbursementAmount.compareTo(product.maxPrincipalAmount()) > 0) {
            return false;
        }

        return true;
    }
}
