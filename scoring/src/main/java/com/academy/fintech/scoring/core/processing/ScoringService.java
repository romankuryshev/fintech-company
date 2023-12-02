package com.academy.fintech.scoring.core.processing;

import com.academy.fintech.scoring.core.pe.client.ProductEngineClientService;
import com.academy.fintech.scoring.core.processing.model.ClientStatistic;
import com.academy.fintech.scoring.core.processing.model.ProcessingResult;
import com.academy.fintech.scoring.core.processing.model.Product;
import com.academy.fintech.scoring.public_interface.processing.dto.ProcessApplicationRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class ScoringService {

    private static final String PRODUCT_CODE = "CL1.0";

    private static final BigDecimal INTEREST = BigDecimal.valueOf(8);

    private static final int TERM_IN_MONTHS = 12;

    private final ProductEngineClientService productEngineClientService;

    public ProcessingResult process(ProcessApplicationRequestDto requestDto) {
        Product product = productEngineClientService.getProductInfo(PRODUCT_CODE);
        ClientStatistic clientStatistic = productEngineClientService.getClientStatistic(requestDto.clientId());
        BigDecimal paymentAmount = productEngineClientService.getPaymentAmount(INTEREST, TERM_IN_MONTHS, requestDto.disbursementAmount());

        if (!isValid(product, requestDto.disbursementAmount())) {
            return ProcessingResult.CLOSED;
        }

        int clientScore = calculateClientScore(clientStatistic, paymentAmount, requestDto.clientSalary());
        if (clientScore <= 0) {
            return ProcessingResult.CLOSED;
        }
        return ProcessingResult.ACCEPTED;
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

    /**
     * Метод рассчитывает оценку клиента исходя из его кредитной истории.
     * Наличие каждого просроченного кредита уменьшает оценку на 1. Соответственно в сумме, она уменьшится на
     * общее количество просроченных кредитов.
     * Аналогично, количество кредитов, оплаченных в срок, высчитывается как {@code countProducts - countOverdueProducts}.
     *
     * @param statistic  статистика по продуктам клиента
     * @param paymentAmount  сумма ежемесячного платежа, при одобрении кредита
     * @param clientSalary  заработная плата клиента
     * @return оценка клиента.
     */
    private int calculateClientScore(ClientStatistic statistic, BigDecimal paymentAmount, BigDecimal clientSalary) {
        int clientScore = 0;

        if (clientSalary.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP).compareTo(paymentAmount) > 0) {
            clientScore += 1;
        }

        if (statistic.countProducts() == 0) {
            clientScore += 1;
        } else {
            clientScore += statistic.countProducts() - statistic.countOverdueProducts();
            clientScore -= statistic.countOverdueProducts();
        }

        return clientScore;
    }
}
