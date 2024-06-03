package com.academy.fintech.api.core.application;

import com.academy.fintech.api.core.origination.client.OriginationClientService;
import com.academy.fintech.api.core.origination.client.dto.OriginationCreateRequest;
import com.academy.fintech.api.public_interface.application.dto.CreateApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final OriginationClientService originationClientService;

    public String createApplication(CreateApplicationRequest applicationDto) {
        return originationClientService.createApplication(applicationDto);
    }
}
