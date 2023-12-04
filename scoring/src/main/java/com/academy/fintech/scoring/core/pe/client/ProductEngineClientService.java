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
import com.academy.fintech.scoring.core.processing.model.AgreementDto;
import com.academy.fintech.scoring.core.processing.model.Product;
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

    public Product getProductInfo(String productCode) {
        ProductRequest request = productMapper.toRequest(productCode);

        ProductResponse response = productEngineGrpcClient.getProductInfo(request);

        return productMapper.toModel(response);
    }

    public List<AgreementDto> getClientStatistic(UUID clientId) {
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
