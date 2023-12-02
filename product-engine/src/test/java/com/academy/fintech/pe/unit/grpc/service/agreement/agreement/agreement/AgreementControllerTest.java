package com.academy.fintech.pe.unit.grpc.service.agreement.agreement.agreement;

import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.exception.InvalidAgreementParametersException;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementController;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementRequest;
import com.academy.fintech.pe.grpc.service.agreement.agreement.AgreementResponse;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import com.academy.fintech.pe.grpc.service.agreement.agreement.mapper.AgreementMapper;
import com.academy.fintech.pe.grpc.service.agreement.agreement.mapper.AgreementMapperImpl;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementControllerTest {

    @Mock
    private AgreementCreationService agreementCreationService;

    @Spy
    private AgreementMapper mapper = new AgreementMapperImpl();

    @InjectMocks
    private AgreementController agreementController;

    @Test
    void givenValidAgreementRequest_whenCreate_thenReturnResponseOk() throws Exception {
        Agreement agreement = new Agreement();
        agreement.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        AgreementRequest request = createAgreementRequest();
        StreamRecorder<AgreementResponse> responseObserver = StreamRecorder.create();
        when(agreementCreationService.createAgreement(any(AgreementDto.class))).thenReturn(agreement);

        agreementController.create(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            Assertions.fail("The call did not terminate in time");
        }

        List<AgreementResponse> results = responseObserver.getValues();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getId()).isEqualTo("00000000-0000-0000-0000-000000000002");
    }

    @Test
    void givenInvalidAgreementRequest_whenCreate_thenReturnResponseError() throws Exception {
        AgreementRequest request = createAgreementRequest();
        StreamRecorder<AgreementResponse> responseObserver = StreamRecorder.create();
        when(agreementCreationService.createAgreement(any(AgreementDto.class))).thenThrow(new InvalidAgreementParametersException(List.of("Invalid params")));

        assertThrows(InvalidAgreementParametersException.class, () -> agreementController.create(request, responseObserver));
//        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
//            Assertions.fail("The call did not terminate in time");
//        }
    }

    private AgreementRequest createAgreementRequest() {
        return AgreementRequest.newBuilder()
                .setProductCode("CL2.0")
                .setClientId("00000000-0000-0000-0000-000000000001")
                .setInterest("13")
                .setTerm(12)
                .setDisbursementAmount("50000")
                .setOriginationAmount("2000")
                .build();
    }
}