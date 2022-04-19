package com.abraaofaher.studyUUID.model.repositories;

import com.abraaofaher.studyUUID.model.entities.CommonObject;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICommonRepository extends IDefaultRepositoryInterface<CommonObject, UUID> {
    Optional<CommonObject> findByCpf(String cpf);
}