package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.CreateApplicationRequest;
import com.academy.fintech.application.CreateRequest;
import com.academy.fintech.application.CreateResponse;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OriginationClientService {

    private final OriginationGrpcClient originationGrpcClient;

    public String createApplication(CreateApplicationRequest applicationRequest) {
        CreateRequest request = mapDtoToRequest(applicationRequest);

        CreateResponse response;
        try {
            response = originationGrpcClient.createApplication(request);
        } catch (StatusRuntimeException e) {
            String applicationId = e.getTrailers().get(Metadata.Key.of("applicationid", Metadata.ASCII_STRING_MARSHALLER));
            response = CreateResponse.newBuilder()
                    .setApplicationId(applicationId)
                    .build();
        }

        return response.getApplicationId();
    }

    private static CreateRequest mapDtoToRequest(CreateApplicationRequest applicationRequest) {
        return CreateRequest.newBuilder()
                .setEmail((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .setSalary(Integer.toString(applicationRequest.salary()))
                .setDisbursementAmount(Integer.toString(applicationRequest.amount()))
                .setProductCode(applicationRequest.productCode())
                .setTermInMonths(applicationRequest.termInMonths())
                .setInterest(applicationRequest.interest())
                .build();
    }

}
