package com.academy.fintech.pe.unit.core.service.agreement.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgreementServiceTest {

    @Mock
    private AgreementRepository agreementRepository;

    @InjectMocks
    private AgreementService agreementService;

    @Test
    void givenValidAgreementId_whenGetById_thenReturnAgreement() {
        Agreement expectedAgreement = new Agreement();
        expectedAgreement.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        when(agreementRepository.findById(expectedAgreement.getId())).thenReturn(Optional.of(expectedAgreement));

        Agreement actualAgreement = agreementService.getById(expectedAgreement.getId());

        assertThat(actualAgreement).isEqualTo(expectedAgreement);
    }

    @Test
    void givenInvalidAgreementId_whenGetById_thenReturnNull() {
        Agreement expectedAgreement = new Agreement();
        expectedAgreement.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        when(agreementRepository.findById(expectedAgreement.getId())).thenReturn(Optional.empty());

        Agreement actualAgreement = agreementService.getById(expectedAgreement.getId());

        assertThat(actualAgreement).isNull();
    }

    @Test
    void givenAgreementDto_whenCreateAgreement_thenCreateSuccess() {
        Product product = createProduct();
        AgreementDto dto = new AgreementDto(product.getCode(),
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                BigDecimal.valueOf(13),
                12,
                BigDecimal.valueOf(60_000),
                BigDecimal.valueOf(2000));
        when(agreementRepository.save(any())).thenAnswer(method -> method.getArguments()[0]);

        Agreement actualAgreement = agreementService.create(dto, product);

        assertEquals(dto.clientId(), actualAgreement.getClientId());
        assertEquals(product, actualAgreement.getProduct());
        assertEquals(dto.interest(), actualAgreement.getInterest());
        assertEquals(dto.term(), actualAgreement.getTermInMonths());
        assertEquals(dto.principalAmount(), actualAgreement.getPrincipalAmount());
        assertEquals(dto.originationAmount(), actualAgreement.getOriginationAmount());
    }

    private Product createProduct() {
        Product newProduct = new Product();
        newProduct.setCode("CL2.0");
        newProduct.setMinTerm(3);
        newProduct.setMaxTerm(20);
        newProduct.setMinPrincipalAmount(BigDecimal.valueOf(50_000));
        newProduct.setMaxPrincipalAmount(BigDecimal.valueOf(600_000));
        newProduct.setMinInterest(BigDecimal.valueOf(10));
        newProduct.setMaxInterest(BigDecimal.valueOf(21));
        newProduct.setMinOriginationAmount(BigDecimal.valueOf(1000));
        newProduct.setMaxOriginationAmount(BigDecimal.valueOf(5000));
        return newProduct;
    }
}

