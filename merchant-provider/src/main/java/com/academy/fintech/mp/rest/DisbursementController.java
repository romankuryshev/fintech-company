package com.academy.fintech.mp.rest;

import com.academy.fintech.mp.public_interface.CreateDisbursementRequest;
import com.academy.fintech.mp.core.DisbursementCreationService;
import com.academy.fintech.mp.public_interface.DisbursementStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DisbursementController {

    private final DisbursementCreationService disbursementCreationService;

    @PostMapping("/disbursements")
    public void createDisbursement(@RequestBody CreateDisbursementRequest request) {
        disbursementCreationService.createDisbursement(request);
    }

    @GetMapping("/disbursements/{agreementId}/status")
    public DisbursementStatusResponse getStatus(@PathVariable UUID agreementId) {
        return disbursementCreationService.getStatus(agreementId);
    }
}
