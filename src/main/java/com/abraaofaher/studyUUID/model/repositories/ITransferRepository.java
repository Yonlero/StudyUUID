package com.abraaofaher.studyUUID.model.repositories;

import com.abraaofaher.studyUUID.model.entities.TransferInformation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ITransferRepository extends PagingAndSortingRepository<TransferInformation, UUID> {
//    @Query("""
//                SELECT T FROM TransferInformation T, CommonObject C WHERE C.id IS NOT NULL
//            """)
//    Page<TransferInformation> getAllTransfer(Pageable pageable);
}