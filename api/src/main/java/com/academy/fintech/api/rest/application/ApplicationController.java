package com.academy.fintech.api.rest.application;

import com.academy.fintech.api.core.application.ApplicationService;
import com.academy.fintech.api.public_interface.application.dto.CreateApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public String create(@RequestBody CreateApplicationRequest applicationRequest) {
        return applicationService.createApplication(applicationRequest);
    }

}
