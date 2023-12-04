package com.academy.fintech.pe.grpc.service.agreement.statistic;

import com.academy.fintech.pe.core.service.agreement.PaymentScheduleCreationService;
import com.academy.fintech.pe.core.service.agreement.StatisticService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.grpc.service.agreement.statistic.dto.AdvancedPaymentRequestDto;
import com.academy.fintech.pe.grpc.service.agreement.statistic.dto.ClientAgreementDto;
import com.academy.fintech.pe.grpc.service.agreement.statistic.mapper.AdvancedPaymentMapper;
import com.academy.fintech.pe.grpc.service.agreement.statistic.mapper.ClientAgreementsMapper;
import com.academy.fintech.pe.grpc.service.agreement.statistic.mapper.ProductMapper;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentRequest;
import com.academy.fintech.scoring.application_processing.AdvancedPaymentResponse;
import com.academy.fintech.scoring.application_processing.ClientAgreementsRequest;
import com.academy.fintech.scoring.application_processing.ClientAgreementsResponse;
import com.academy.fintech.scoring.application_processing.ProductRequest;
import com.academy.fintech.scoring.application_processing.ProductResponse;
import com.academy.fintech.scoring.application_processing.ScoringDataServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;
import java.util.UUID;

@GrpcService
@AllArgsConstructor
public class StatisticController extends ScoringDataServiceGrpc.ScoringDataServiceImplBase {

    private final StatisticService statisticService;

    private final PaymentScheduleCreationService paymentScheduleCreationService;

    private final AdvancedPaymentMapper advancedPaymentMapper;

    private final ProductMapper productMapper;

    private final ClientAgreementsMapper clientAgreementsMapper;

    @Override
    public void getProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        Product product = statisticService.getProduct(request.getProductCode());

        ProductResponse response = productMapper.toResponse(product);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getClientAgreements(ClientAgreementsRequest request, StreamObserver<ClientAgreementsResponse> responseObserver) {
        ClientAgreementDto agreementDto = statisticService.getClientAgreements(UUID.fromString(request.getClientId()));

        ClientAgreementsResponse response = clientAgreementsMapper.toResponse(agreementDto);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPaymentAmount(AdvancedPaymentRequest request, StreamObserver<AdvancedPaymentResponse> responseObserver) {
        AdvancedPaymentRequestDto requestDto = advancedPaymentMapper.toDto(request);

        BigDecimal paymentAmount = paymentScheduleCreationService.calculateAdvancedPayment(requestDto);
        AdvancedPaymentResponse response = advancedPaymentMapper.toResponse(paymentAmount);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}