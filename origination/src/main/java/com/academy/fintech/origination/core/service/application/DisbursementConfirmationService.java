package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.pe.client.ProductEngineClientService;
import com.academy.fintech.origination.core.service.application.domain_service.ApplicationOperationService;
import com.academy.fintech.pg.payment_service.ChangeStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisbursementConfirmationService {

    private final ApplicationOperationService applicationOperationService;

    private final ProductEngineClientService productEngineClientService;

}
