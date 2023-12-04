package com.academy.fintech.pe.grpc.service.agreement.statistic.mapper;

import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.scoring.application_processing.ProductRequest;
import com.academy.fintech.scoring.application_processing.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "minTermInMonths", source = "minTerm")
    @Mapping(target = "maxTermInMonths", source = "maxTerm")
    ProductResponse toResponse(Product product);
}
