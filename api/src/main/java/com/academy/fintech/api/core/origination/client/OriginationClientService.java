package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.CreateRequest;
import com.academy.fintech.application.CreateResponse;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OriginationClientService {

    private final OriginationGrpcClient originationGrpcClient;

    public String createApplication(ApplicationDto applicationDto) {
        CreateRequest request = mapDtoToRequest(applicationDto);

        CreateResponse response;
        try {
            response = originationGrpcClient.createApplication(request);
        } catch (StatusRuntimeException e) {
            String applicationId = e.getTrailers().get(Metadata.Key.of("applicationid", Metadata.ASCII_STRING_MARSHALLER));
            response = CreateResponse.newBuilder()
                    .setApplicationId(applicationId)
                    .build();
        }

        log.info(response.getApplicationId());

        return response.getApplicationId();
    }

    private static CreateRequest mapDtoToRequest(ApplicationDto applicationDto) {
        return CreateRequest.newBuilder()
                .setFirstName(applicationDto.firstName())
                .setLastName(applicationDto.lastName())
                .setEmail(applicationDto.email())
                .setSalary(Integer.toString(applicationDto.salary()))
                .setDisbursementAmount(Integer.toString(applicationDto.amount()))
                .build();
    }

}
