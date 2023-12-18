package com.academy.fintech.scoring.unit.core.pe.client.client;

import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentResponse;
import com.academy.fintech.scoring.application_processing.AgreementResponse;
import com.academy.fintech.scoring.application_processing.ClientAgreementsRequest;
import com.academy.fintech.scoring.application_processing.ClientAgreementsResponse;
import com.academy.fintech.scoring.application_processing.ProductRequest;
import com.academy.fintech.scoring.application_processing.ProductResponse;
import com.academy.fintech.scoring.core.pe.client.ProductEngineClientService;
import com.academy.fintech.scoring.core.pe.client.grpc.ProductEngineGrpcClient;
import com.academy.fintech.scoring.core.pe.client.mapper.AdvancedPaymentMapper;
import com.academy.fintech.scoring.core.pe.client.mapper.AdvancedPaymentMapperImpl;
import com.academy.fintech.scoring.core.pe.client.mapper.AgreementMapper;
import com.academy.fintech.scoring.core.pe.client.mapper.AgreementMapperImpl;
import com.academy.fintech.scoring.core.pe.client.mapper.ClientAgreementMapper;
import com.academy.fintech.scoring.core.pe.client.mapper.ClientAgreementMapperImpl;
import com.academy.fintech.scoring.core.pe.client.mapper.ProductMapper;
import com.academy.fintech.scoring.core.pe.client.mapper.ProductMapperImpl;
import com.academy.fintech.scoring.core.processing.dto.AgreementDto;
import com.academy.fintech.scoring.core.processing.model.Product;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductEngineClientServiceTest {

    @Spy
    private ProductMapper productMapper = new ProductMapperImpl();

    @Spy
    private AdvancedPaymentMapper advancedPaymentMapper = new AdvancedPaymentMapperImpl();

    @Spy
    private AgreementMapper agreementMapper = new AgreementMapperImpl();

    @Spy
    @InjectMocks
    private ClientAgreementMapper clientAgreementMapper = new ClientAgreementMapperImpl();

    @Mock
    private ProductEngineGrpcClient productEngineGrpcClient;

    @InjectMocks
    private ProductEngineClientService productEngineClientService;

    @Test
    void givenValidProductCode_whenGetProduct_thenReturnProduct() {
        ProductResponse response = ProductResponse.newBuilder()
                .setCode("CL1.0")
                .setMinInterest("8")
                .setMaxInterest("15")
                .setMinTermInMonths(3)
                .setMaxTermInMonths(20)
                .setMinPrincipalAmount("20000")
                .setMaxPrincipalAmount("200000")
                .build();

        when(productEngineGrpcClient.getProduct(any(ProductRequest.class))).thenReturn(response);

        Product actualProduct = productEngineClientService.getProduct("CL1.0");

        assertThat(actualProduct.code()).isEqualTo("CL1.0");
        assertThat(actualProduct.minInterest()).isEqualTo(BigDecimal.valueOf(8));
        assertThat(actualProduct.maxInterest()).isEqualTo(BigDecimal.valueOf(15));
        assertThat(actualProduct.minTermInMonths()).isEqualTo(3);
        assertThat(actualProduct.maxTermInMonths()).isEqualTo(20);
        assertThat(actualProduct.minPrincipalAmount()).isEqualTo(BigDecimal.valueOf(20000));
        assertThat(actualProduct.maxPrincipalAmount()).isEqualTo(BigDecimal.valueOf(200000));
    }

    @Test
    void givenInvalidProductCode_whenGetProduct_thenThrowsException() {
        when(productEngineGrpcClient.getProduct(any(ProductRequest.class))).thenThrow(new StatusRuntimeException(Status.INVALID_ARGUMENT));

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class,
                () -> productEngineClientService.getProduct("CL1.0"));

        assertThat(exception.getStatus().getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
    }

    @Test
    void givenValidClientId_whenGetClientAgreements_thenReturnAgreements() {
        AgreementResponse response1 = AgreementResponse.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000001")
                .setNextPaymentDate(LocalDate.now().minusDays(8).toString())
                .build();
        AgreementResponse response2 = AgreementResponse.newBuilder()
                .setAgreementId("00000000-0000-0000-0000-000000000002")
                .setNextPaymentDate(LocalDate.now().minusDays(9).toString())
                .build();
        List<AgreementResponse> agreementResponses = List.of(response1, response2);
        ClientAgreementsResponse response = ClientAgreementsResponse.newBuilder()
                .addAllAgreements(agreementResponses)
                .build();
        when(productEngineGrpcClient.getClientAgreements(any(ClientAgreementsRequest.class))).thenReturn(response);

        List<AgreementDto> actualAgreements = productEngineClientService.getClientAgreements(UUID.fromString("00000000-0000-0000-0000-000000000001"));

        assertThat(actualAgreements.isEmpty()).isFalse();
        assertThat(actualAgreements.get(0).agreementId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThat(actualAgreements.get(0).nextPaymentDate()).isEqualTo(LocalDate.parse(LocalDate.now().minusDays(8).toString()));
        assertThat(actualAgreements.get(1).agreementId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        assertThat(actualAgreements.get(1).nextPaymentDate()).isEqualTo(LocalDate.parse(LocalDate.now().minusDays(9).toString()));
    }

    @Test
    void givenValidParams_whenGetPaymentAmount_thenReturnPaymentAmount() {
        AdvancedPaymentResponse response = AdvancedPaymentResponse.newBuilder()
                .setPaymentAmount("20000")
                .build();
        when(productEngineGrpcClient.getPaymentAmount(any(AdvancedPaymentRequest.class))).thenReturn(response);

        BigDecimal actualAmount = productEngineClientService.getPaymentAmount(BigDecimal.valueOf(11), 5, BigDecimal.valueOf(100000));

        assertThat(actualAmount).isEqualTo(BigDecimal.valueOf(20000));
    }
}