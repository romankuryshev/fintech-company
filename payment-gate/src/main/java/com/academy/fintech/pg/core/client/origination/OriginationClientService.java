package com.academy.fintech.pg.core.client.origination;

import com.academy.fintech.pg.core.client.origination.grpc.OriginationGrpcClient;
import com.academy.fintech.pg.core.disbursement.db.Disbursement;
import com.academy.fintech.pg.payment_service.ChangeStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OriginationClientService {

    private final OriginationGrpcClient originationRestClient;

    public void confirmDisbursement(Disbursement disbursement) {
        originationRestClient.changeStatus(mapToChangeStatusRequest(disbursement));
    }

    private ChangeStatusRequest mapToChangeStatusRequest(Disbursement disbursement) {
        return ChangeStatusRequest.newBuilder()
                .setAgreementId(disbursement.getAgreementId().toString())
                .setStatus(disbursement.getStatus().toString())
                .build();
    }
}
