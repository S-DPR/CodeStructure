package com.example.codestructure.transaction_sample.repos;

import com.example.codestructure.transaction_sample.entity.EntityA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface EntityARepos extends JpaRepository<EntityA, UUID> {
    @Lock(PESSIMISTIC_WRITE)
    @Query("SELECT a FROM EntityA a WHERE a.id = :id")
    Optional<EntityA> findByIdWithLock(UUID id);
}
