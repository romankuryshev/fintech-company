package com.academy.fintech.pe.grpc.service.agreement.statistic.dto;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ClientAgreementDto {
    List<Agreement> agreements;
}
