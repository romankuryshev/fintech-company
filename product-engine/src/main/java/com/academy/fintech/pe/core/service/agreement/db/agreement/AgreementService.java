package com.academy.fintech.pe.core.service.agreement.db.agreement;

import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.dwh.KafkaSenderService;
import com.academy.fintech.pe.grpc.service.agreement.agreement.dto.AgreementDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;
    public final KafkaSenderService kafkaSenderService;

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

        return saveAndSendToDwh(agreement);
    }

    @Nullable
    public Agreement getById(UUID agreementId) {
        Optional<Agreement> agreement = agreementRepository.findById(agreementId);
        return agreement.orElse(null);
    }

    public List<Agreement> getAllNewOverdueAgreements() {
        return agreementRepository.findAllByNextPaymentDateAfter(LocalDate.now());
    }

    public List<Agreement> getAllAgreementsByClientId(UUID clientId) {
        return agreementRepository.findAllByClientId(clientId);
    }

    public Agreement save(Agreement agreement) {
        return agreementRepository.save(agreement);
    }

    public Agreement saveAndSendToDwh(Agreement agreement) {
        agreement = agreementRepository.save(agreement);
        try {
            kafkaSenderService.createMessage(agreement);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return agreement;
    }
}
