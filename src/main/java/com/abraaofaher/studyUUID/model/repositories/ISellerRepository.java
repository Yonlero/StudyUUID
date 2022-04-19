package com.abraaofaher.studyUUID.model.repositories;

import com.abraaofaher.studyUUID.model.entities.SellerObject;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ISellerRepository extends IDefaultRepositoryInterface<SellerObject, UUID> {
    Optional<SellerObject> findByCnpj(String cnpj);
}