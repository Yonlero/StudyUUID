package com.abraaofaher.studyUUID.model.services.interfaces;

import com.abraaofaher.studyUUID.model.entities.TransferInformation;
import com.abraaofaher.studyUUID.model.services.TransferServiceImp.TransferBodyRequest;
import com.abraaofaher.studyUUID.model.services.TransferServiceImp.TransferBodyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITransferDefaultService {
    //---Default Methods to CRUD---
    Page<TransferInformation> getTransfer(Pageable pageable);

    TransferInformation getTransferByUser(String identify);

    TransferBodyResponse makeNewTransfer(TransferBodyRequest transferInformation);

    TransferInformation getTransferInformationById(UUID transaction_id);
}