package com.academy.fintech.scoring.grpc.mapper;

import com.academy.fintech.scoring.application_processing.ProcessApplicationRequest;
import com.academy.fintech.scoring.application_processing.ProcessApplicationResponse;
import com.academy.fintech.scoring.core.processing.model.ProcessingResult;
import com.academy.fintech.scoring.public_interface.processing.dto.ProcessApplicationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProcessMapper {

    ProcessApplicationRequestDto toDto (ProcessApplicationRequest request);

    ProcessApplicationResponse toResponse(ProcessingResult result);
}
