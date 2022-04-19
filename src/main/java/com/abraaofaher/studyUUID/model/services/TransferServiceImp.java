package com.abraaofaher.studyUUID.model.services;

import com.abraaofaher.studyUUID.model.entities.CommonObject;
import com.abraaofaher.studyUUID.model.entities.TransferInformation;
import com.abraaofaher.studyUUID.model.entities.User;
import com.abraaofaher.studyUUID.model.enums.TransactionStatus;
import com.abraaofaher.studyUUID.model.exceptions.InvalidIdentify;
import com.abraaofaher.studyUUID.model.exceptions.NotFoundException;
import com.abraaofaher.studyUUID.model.repositories.ICommonRepository;
import com.abraaofaher.studyUUID.model.repositories.ISellerRepository;
import com.abraaofaher.studyUUID.model.repositories.ITransferRepository;
import com.abraaofaher.studyUUID.model.services.interfaces.ITransferDefaultService;
import com.abraaofaher.studyUUID.model.utils.RepositoryManagement;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.abraaofaher.studyUUID.model.enums.TransactionStatus.*;
import static com.abraaofaher.studyUUID.model.services.UserServiceImp.AuthorizationResponse.getExternalAuthentication;

@Service
public record TransferServiceImp(ITransferRepository transferRepository,
                                 RepositoryManagement repositoryManagement,
                                 ICommonRepository commonRepository,
                                 ISellerRepository sellerRepository) implements ITransferDefaultService {
    @Override
    public Page<TransferInformation> getTransfer(Pageable pageable) {
        return transferRepository.findAll(pageable);
    }

    @Override
    public TransferInformation getTransferInformationById(UUID transaction_id) {
        return transferRepository.findById(transaction_id).orElseThrow(() -> new NotFoundException("Transaction ID Not found"));
    }

    @Override
    public TransferInformation getTransferByUser(String identify) {
        return null;
    }

    @Override
    public TransferBodyResponse makeNewTransfer(TransferBodyRequest transferInformation) {
        TransferBodyResponse response = TransferBodyResponse.builder()
                .payerIdentification(transferInformation.getPayingIdentification())
                .receiverIdentification(transferInformation.getReceiverIdentification())
                .amountToTransfer(transferInformation.getAmountToTransfer())
                .transactionStatus(UNAUTHORIZED)
                .build();
        if (transferMoney(transferInformation))
            response.setTransactionStatus(COMPLETED);
        return response;
    }

    private boolean transferMoney(TransferBodyRequest transferInformation) {
        // Get user in DB and convert to CommonObject
        CommonObject paying = commonRepository.findByCpf(
                        transferInformation.getPayingIdentification().length() == 11 ? transferInformation.getPayingIdentification() : "0")
                .orElseThrow(() -> new NotFoundException("Identify not found"));
        User receiver = switch (transferInformation.getReceiverIdentification().length()) {
            case 11 -> commonRepository.findByCpf(transferInformation.getReceiverIdentification()).orElseThrow(() -> new NotFoundException("Identify not found"));
            case 14 -> sellerRepository.findByCnpj(transferInformation.getReceiverIdentification()).orElseThrow(() -> new NotFoundException("Identify not found"));
            default -> throw new InvalidIdentify("Illegal identify");
        };

        // Check authorization and amount
        if (paying.getAmount().compareTo(new BigInteger(transferInformation.getAmountToTransfer())) < 0)
            return false;
        if (!getExternalAuthentication().getMessage().equals("Autorizado"))
            return false;

        // Apply changes in DB
        commonRepository.updateAmount(paying.getAmount().subtract(new BigInteger(transferInformation.getAmountToTransfer())), paying.getEmail());
        repositoryManagement.getRepositoryBasedOnInstance(receiver).updateAmount(receiver.getAmount().add(new BigInteger(transferInformation.getAmountToTransfer())), receiver.getEmail());
        transferRepository.save(TransferInformation.builder()
                .amount(new BigInteger(transferInformation.getAmountToTransfer()))
                .paying(paying)
                .receiver(receiver)
                .status(COMPLETED)
                .build());
        return true;
    }

    public TransferBodyResponse refundMoney(String transaction_id) {
        TransferInformation transferInformation = getTransferInformationById(UUID.fromString(transaction_id));
        CommonObject paying = transferInformation.getPaying();
        User receiver = transferInformation.getReceiver();
        transferInformation.setStatus(CANCELED);

        commonRepository.updateAmount(paying.getAmount().add(new BigInteger(String.valueOf(transferInformation.getAmount()))), paying.getEmail());
        repositoryManagement.getRepositoryBasedOnInstance(receiver).updateAmount(receiver.getAmount().subtract(new BigInteger(String.valueOf(transferInformation.getAmount()))), receiver.getEmail());
        transferRepository.save(transferInformation);
        return TransferBodyResponse.builder()
                .transactionStatus(transferInformation.getStatus())
                .payerIdentification(transferInformation.getPaying().getCpf())
                .amountToTransfer(transferInformation.getAmount().toString())
                .build();
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferBodyRequest {
        private String payingIdentification;
        private String amountToTransfer;
        private String receiverIdentification;
        @JsonIgnore
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
        private final LocalDateTime transactionTime = LocalDateTime.now();
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferBodyResponse {
        private String receiverIdentification;
        private String payerIdentification;
        private String amountToTransfer;
        private TransactionStatus transactionStatus;
        @JsonIgnore
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
        private final LocalDateTime transactionTime = LocalDateTime.now();
    }
}