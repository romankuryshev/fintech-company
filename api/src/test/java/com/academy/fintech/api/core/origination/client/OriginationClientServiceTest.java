package com.academy.fintech.api.core.origination.client;


import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.CreateRequest;
import com.academy.fintech.application.CreateResponse;
import io.grpc.Metadata;
import io.grpc.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.grpc.Status.ALREADY_EXISTS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OriginationClientServiceTest {

    @Mock
    private OriginationGrpcClient client;

    @InjectMocks
    private OriginationClientService clientService;

    @Test
    void givenValidDto_whenCreate_thenReturnNewApplicationId() {
        CreateResponse expectedResponse = CreateResponse.newBuilder()
                .setApplicationId("00000000-0000-0000-0000-000000000001")
                .build();
        ApplicationDto dto = createDto();
        when(client.createApplication(any(CreateRequest.class))).thenReturn(expectedResponse);

        String applicationId = clientService.createApplication(dto);

        assertThat(applicationId).isEqualTo(expectedResponse.getApplicationId());
    }

    @Test
    void givenInvalidDto_whenCreate_thenReturnNewApplicationId() {
        ApplicationDto dto = createDto();
        Status status = ALREADY_EXISTS.withDescription("Already exists");
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("applicationId", Metadata.ASCII_STRING_MARSHALLER), "00000000-0000-0000-0000-000000000001");
        when(client.createApplication(any(CreateRequest.class))).thenThrow(status.asRuntimeException(metadata));

        String result = clientService.createApplication(dto);

        assertThat(result).isEqualTo("00000000-0000-0000-0000-000000000001");
    }

    private ApplicationDto createDto() {
        return new ApplicationDto("roman", "kuryshev", "roman@mail.ru", 30_000, 100_000);
    }
}