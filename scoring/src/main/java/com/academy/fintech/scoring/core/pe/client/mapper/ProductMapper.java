package com.academy.fintech.scoring.core.pe.client.mapper;

import com.academy.fintech.scoring.application_processing.ProductRequest;
import com.academy.fintech.scoring.application_processing.ProductResponse;
import com.academy.fintech.scoring.core.processing.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product toModel(ProductResponse response);

    ProductRequest toRequest(String productCode);
}
