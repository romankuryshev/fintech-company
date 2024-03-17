package com.academy.fintech.pg.public_interface.mapper;

import com.academy.fintech.pg.core.disbursement.db.Disbursement;
import com.academy.fintech.pg.public_interface.DisbursementDto;
import com.academy.fintech.pg.payment_service.DisbursementRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DisbursementMapper {

    DisbursementDto requestToDto(DisbursementRequest request);

    DisbursementDto entityToDto(Disbursement disbursement);
}
