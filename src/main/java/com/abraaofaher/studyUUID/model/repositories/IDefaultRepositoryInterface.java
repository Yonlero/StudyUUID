package com.abraaofaher.studyUUID.model.repositories;

import com.abraaofaher.studyUUID.model.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Optional;

@NoRepositoryBean
@Transactional
public interface IDefaultRepositoryInterface<T, K> extends PagingAndSortingRepository<T, K> {
    Optional<User> findByEmail(@Param("email") String email);
    @Modifying
    @Query("UPDATE #{#entityName} T SET T.amount = :amount WHERE T.email = :identify")
    void updateAmount(@Param("amount") BigInteger amount, @Param("identify") String identify);
}