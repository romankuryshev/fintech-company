package com.academy.fintech.scoring.unit.core.processing;

import com.academy.fintech.scoring.core.pe.client.ProductEngineClientService;
import com.academy.fintech.scoring.core.processing.ScoringService;
import com.academy.fintech.scoring.core.processing.model.AgreementDto;
import com.academy.fintech.scoring.core.processing.model.ProcessingResult;
import com.academy.fintech.scoring.core.processing.model.Product;
import com.academy.fintech.scoring.public_interface.processing.dto.ProcessApplicationRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {

    @Mock
    private ProductEngineClientService productEngineClientService;

    @InjectMocks
    private ScoringService scoringService;

    @Test
    void givenValidRequestDto_whenProcess_thenReturnAccepted() {
        List<AgreementDto> agreementDto = List.of(new AgreementDto(UUID.randomUUID(), LocalDate.parse("2023-12-05")),
                new AgreementDto(UUID.randomUUID(), LocalDate.parse(LocalDate.now().minusDays(5).toString())));
        ProcessApplicationRequestDto requestDto = ProcessApplicationRequestDto.builder()
                .clientId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .applicationId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .clientSalary(BigDecimal.valueOf(60_000))
                .disbursementAmount(BigDecimal.valueOf(100_000))
                .build();
        when(productEngineClientService.getProduct("CL1.0")).thenReturn(createProduct());
        when(productEngineClientService.getClientAgreements(any(UUID.class))).thenReturn(agreementDto);
        when(productEngineClientService.getPaymentAmount(any(BigDecimal.class), anyInt(), any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(50_000));

        ProcessingResult result = scoringService.process(requestDto);

        assertThat(result).isEqualTo(ProcessingResult.ACCEPTED);
    }

    @Test
    void givenTwoOverdueAgreements_whenProcess_thenReturnCanceled() {
        List<AgreementDto> agreementDto = List.of(new AgreementDto(UUID.randomUUID(), LocalDate.parse("2023-11-20")),
                new AgreementDto(UUID.randomUUID(), LocalDate.parse(LocalDate.now().toString())),
                new AgreementDto(UUID.randomUUID(), LocalDate.parse(LocalDate.now().minusDays(4).toString())));
        ProcessApplicationRequestDto requestDto = ProcessApplicationRequestDto.builder()
                .clientId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .applicationId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .clientSalary(BigDecimal.valueOf(60_000))
                .disbursementAmount(BigDecimal.valueOf(100_000))
                .build();
        when(productEngineClientService.getProduct("CL1.0")).thenReturn(createProduct());
        when(productEngineClientService.getClientAgreements(any(UUID.class))).thenReturn(agreementDto);
        when(productEngineClientService.getPaymentAmount(any(BigDecimal.class), anyInt(), any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(50_000));

        ProcessingResult result = scoringService.process(requestDto);

        assertThat(result).isEqualTo(ProcessingResult.CANCELED);
    }

    @Test
    void givenBadClientSalaryAndOverdueAgreement_whenProcess_thenReturnCanceled() {
        List<AgreementDto> agreementDto = List.of(new AgreementDto(UUID.randomUUID(), LocalDate.parse("2023-11-20")));
        ProcessApplicationRequestDto requestDto = ProcessApplicationRequestDto.builder()
                .clientId(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .applicationId(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .clientSalary(BigDecimal.valueOf(1000))
                .disbursementAmount(BigDecimal.valueOf(100_000))
                .build();
        when(productEngineClientService.getProduct("CL1.0")).thenReturn(createProduct());
        when(productEngineClientService.getClientAgreements(any(UUID.class))).thenReturn(agreementDto);
        when(productEngineClientService.getPaymentAmount(any(BigDecimal.class), anyInt(), any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(50_000));

        ProcessingResult result = scoringService.process(requestDto);

        assertThat(result).isEqualTo(ProcessingResult.CANCELED);
    }

    private Product createProduct() {
        return Product.builder()
                .code("CL1.0")
                .minInterest(BigDecimal.valueOf(8))
                .maxInterest(BigDecimal.valueOf(15))
                .minTermInMonths(5)
                .maxTermInMonths(20)
                .minPrincipalAmount(BigDecimal.valueOf(30_000))
                .maxPrincipalAmount(BigDecimal.valueOf(200_000))
                .build();
    }
}