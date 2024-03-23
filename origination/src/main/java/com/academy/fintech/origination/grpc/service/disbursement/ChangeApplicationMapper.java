package com.academy.fintech.origination.grpc.service.disbursement;

import com.academy.fintech.origination.grpc.service.disbursement.dto.ChangeApplicationStatusDto;
import com.academy.fintech.pg.payment_service.ChangeStatusRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChangeApplicationMapper {

    ChangeApplicationStatusDto toDto(ChangeStatusRequest request);
}
