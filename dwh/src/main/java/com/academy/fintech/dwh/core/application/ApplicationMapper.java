package com.academy.fintech.dwh.core.application;

import com.academy.fintech.dwh.core.application.db.application.ApplicationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationMapper {

    ApplicationEvent toEntity(ApplicationMessage applicationMessage);
}
