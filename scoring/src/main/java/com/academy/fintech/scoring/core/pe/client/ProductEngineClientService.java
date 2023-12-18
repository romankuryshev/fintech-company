package com.academy.fintech.scoring.core.pe.client;

import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentResponse;
import com.academy.fintech.scoring.application_processing.ClientAgreementsRequest;
import com.academy.fintech.scoring.application_processing.ClientAgreementsResponse;
import com.academy.fintech.scoring.application_processing.ProductRequest;
import com.academy.fintech.scoring.application_processing.ProductResponse;
import com.academy.fintech.scoring.core.pe.client.grpc.ProductEngineGrpcClient;
import com.academy.fintech.scoring.core.pe.client.mapper.ClientAgreementMapper;
import com.academy.fintech.scoring.core.pe.client.mapper.AdvancedPaymentMapper;
import com.academy.fintech.scoring.core.pe.client.mapper.ProductMapper;
import com.academy.fintech.scoring.core.processing.exception.ProductDoesNotExists;
import com.academy.fintech.scoring.core.processing.dto.AgreementDto;
import com.academy.fintech.scoring.core.processing.model.Product;
import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ProductEngineClientService {

    private final ProductEngineGrpcClient productEngineGrpcClient;

    private final ProductMapper productMapper;

    private final ClientAgreementMapper clientAgreementMapper;

    private final AdvancedPaymentMapper advancedPaymentMapper;

    public Product getProduct(String productCode) {
        ProductRequest request = productMapper.toRequest(productCode);
        try {
            ProductResponse response = productEngineGrpcClient.getProduct(request);
            return productMapper.toModel(response);
        } catch (StatusRuntimeException e) {
            log.error("Product does not exists code {}", request);
            throw new ProductDoesNotExists("Product with code " + productCode + " does not exists");
        }
    }

    public List<AgreementDto> getClientAgreements(UUID clientId) {
        ClientAgreementsRequest request = clientAgreementMapper.toRequest(clientId);

        ClientAgreementsResponse response = productEngineGrpcClient.getClientAgreements(request);

        return clientAgreementMapper.toModel(response.getAgreementsList());
    }

    public BigDecimal getPaymentAmount(BigDecimal interest, int term, BigDecimal disbursementAmount) {
        AdvancedPaymentRequest request = advancedPaymentMapper.toRequest(interest, term, disbursementAmount);

        AdvancedPaymentResponse response = productEngineGrpcClient.getPaymentAmount(request);

        return new BigDecimal(response.getPaymentAmount());
    }
}
