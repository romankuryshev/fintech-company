package com.academy.fintech.pg.core.client.provider.merchant;

import com.academy.fintech.pg.core.client.provider.merchant.dto.DisbursementStatusResponse;
import com.academy.fintech.pg.core.client.provider.merchant.rest.MerchantProviderRestClient;
import com.academy.fintech.pg.core.disbursement.db.Disbursement;
import com.academy.fintech.pg.core.disbursement.db.DisbursementStatus;
import com.academy.fintech.pg.public_interface.DisbursementDto;
import com.academy.fintech.pg.public_interface.mapper.DisbursementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MerchantProviderService {

    private final DisbursementMapper mapper;

    private final MerchantProviderRestClient merchantProviderRestClient;

    public void sendDisbursement(Disbursement disbursement) {
        DisbursementDto disbursementDto = mapper.entityToDto(disbursement);
        merchantProviderRestClient.sendDisbursement(disbursementDto);
    }

    public DisbursementStatus checkStatus(Disbursement disbursement) {
        DisbursementStatusResponse response = merchantProviderRestClient.checkDisbursement(disbursement.getAgreementId());
        return response.status();
    }
}
