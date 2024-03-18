package com.academy.fintech.mp.rest;

import com.academy.fintech.mp.public_interface.CreateDisbursementRequest;
import com.academy.fintech.mp.core.DisbursementCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DisbursementController {

    private final DisbursementCreationService disbursementCreationService;

    @PostMapping("/disbursement/create")
    public void createDisbursement(@RequestBody CreateDisbursementRequest request) {
        disbursementCreationService.createDisbursement(request);
    }
}
