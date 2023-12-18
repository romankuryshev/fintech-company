package com.academy.fintech.pe.grpc.service.agreement.statistic.dto;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ClientAgreementDto {
    private List<Agreement> agreements;
}
