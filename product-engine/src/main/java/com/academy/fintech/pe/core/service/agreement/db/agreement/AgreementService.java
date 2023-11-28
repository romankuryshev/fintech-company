package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgreementService {

    private final AgreementRepository agreementRepository;

    public AgreementService(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    public Agreement create(AgreementDto agreementDto, Product product) {
        var agreement = Agreement.builder()
                .product(product)
                .clientId(agreementDto.clientId())
                .interest(agreementDto.interest())
                .termInMonths(agreementDto.term())
                .originationAmount(agreementDto.originationAmount())
                .principalAmount(agreementDto.principalAmount())
                .status(AgreementStatus.NEW)
                .build();

        return agreementRepository.save(agreement);
    }

    @Nullable
    public Agreement getById(UUID agreementId) {
        Optional<Agreement> agreement = agreementRepository.findById(agreementId);
        return agreement.orElse(null);
    }

    public Agreement save(Agreement agreement) {
        return agreementRepository.save(agreement);
    }
}
