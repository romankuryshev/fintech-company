package com.academy.fintech.pe.unit.core.service.agreement.agreement;

import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import com.academy.fintech.pe.core.service.agreement.exception.AgreementDoesNotExists;
import com.academy.fintech.pe.core.service.agreement.exception.InvalidAgreementParametersException;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AgreementCreationServiceTest {

    @Mock
    private AgreementService agreementService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AgreementCreationService agreementCreationService;

    private static Product createProduct() {
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

    @Test
    void givenValidAgreementDto_whenCreateAgreement_thenReturnAgreement() {
        // given
        Product product = createProduct();
        AgreementDto dto = new AgreementDto(product.getCode(),
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                BigDecimal.valueOf(13),
                12,
                BigDecimal.valueOf(60_000),
                BigDecimal.valueOf(2000));
        when(productService.getProduct(product.getCode())).thenReturn(product);
        when(agreementService.create(dto, product)).thenReturn(new Agreement());

        // when
        Agreement agreement = agreementCreationService.createAgreement(dto);

        // then
        assertThat(agreement).isInstanceOf(Agreement.class);
    }

    @ParameterizedTest
    @MethodSource("provideDtosForCreateAgreement")
    void givenInvalidAgreementDto_whenCreateAgreement_thenReturnNull(AgreementDto dto) {
        // given
        Product product = createProduct();
        when(productService.getProduct(product.getCode())).thenReturn(product);

        // when
        assertThrows(InvalidAgreementParametersException.class, () -> agreementCreationService.createAgreement(dto));
    }

    private static Stream<Arguments> provideDtosForCreateAgreement() {
        return Stream.of(
                Arguments.of(createDtoWithInvalidTerm()),
                Arguments.of(createDtoWithInvalidInterest()),
                Arguments.of(createDtoWithInvalidPrincipalAmount()),
                Arguments.of(createDtoWithInvalidOriginationAmount())
        );
    }

    private static AgreementDto createDtoWithInvalidTerm() {
        Product product = createProduct();
        return new AgreementDto(product.getCode(),
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                BigDecimal.valueOf(13),
                // invalid value
                2,
                BigDecimal.valueOf(60_000),
                BigDecimal.valueOf(2000));
    }

    private static AgreementDto createDtoWithInvalidInterest() {
        Product product = createProduct();
        return new AgreementDto(product.getCode(),
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                // invalid value
                BigDecimal.valueOf(30),
                12,
                BigDecimal.valueOf(60_000),
                BigDecimal.valueOf(2000));
    }

    private static AgreementDto createDtoWithInvalidPrincipalAmount() {
        Product product = createProduct();
        return new AgreementDto(product.getCode(),
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                BigDecimal.valueOf(13),
                12,
                // invalid value
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(2000));
    }

    private static AgreementDto createDtoWithInvalidOriginationAmount() {
        Product product = createProduct();
        return new AgreementDto(product.getCode(),
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                BigDecimal.valueOf(13),
                12,
                BigDecimal.valueOf(60_000),
                // invalid value
                BigDecimal.valueOf(100));
    }
}
