package com.abraaofaher.studyUUID.controllers;

import com.abraaofaher.studyUUID.model.entities.TransferInformation;
import com.abraaofaher.studyUUID.model.services.TransferServiceImp;
import com.abraaofaher.studyUUID.model.services.TransferServiceImp.TransferBodyRequest;
import com.abraaofaher.studyUUID.model.services.TransferServiceImp.TransferBodyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/study/v1/transfer")
public class TransferController {
    private final TransferServiceImp transferServiceImp;

    public TransferController(TransferServiceImp transferServiceImp) {
        this.transferServiceImp = transferServiceImp;
    }

    //region Get Methods
    @GetMapping
    public ResponseEntity<Page<TransferInformation>> getAllUsers(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "5") Integer size) {
        return ResponseEntity.ok(transferServiceImp.getTransfer(PageRequest.of(page, size)));
    }
    //endregion


    @PostMapping
    public ResponseEntity<TransferBodyResponse> transferMoney(@Valid @RequestBody TransferBodyRequest transferBody) {
        if (Objects.isNull(transferBody.getPayingIdentification()) || Objects.isNull(transferBody.getReceiverIdentification()))
            throw new RuntimeException("Invalid Body");
        return ResponseEntity.ok(transferServiceImp.makeNewTransfer(transferBody));
    }

    @PostMapping("/refundTransfer")
    public ResponseEntity<TransferBodyResponse> refundTransfer(@Valid @RequestBody String uuid) {
        if (Objects.isNull(uuid))
            throw new RuntimeException("Invalid UUID");
        return ResponseEntity.ok(transferServiceImp.refundMoney(uuid));
    }
}