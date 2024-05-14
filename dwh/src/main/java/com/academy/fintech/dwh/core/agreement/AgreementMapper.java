package com.academy.fintech.dwh.core.agreement;

import com.academy.fintech.dwh.core.agreement.db.agreement.AgreementEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgreementMapper {

    @Mapping(target = "agreementId", source = "id")
    AgreementEvent toEntity(AgreementMessage agreementMessage);
}
