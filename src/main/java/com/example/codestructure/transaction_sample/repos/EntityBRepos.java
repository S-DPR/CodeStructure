package com.example.codestructure.transaction_sample.repos;

import com.example.codestructure.transaction_sample.entity.EntityB;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.LockModeType.PESSIMISTIC_READ;

@Repository
public interface EntityBRepos extends JpaRepository<EntityB, UUID> {
    @Lock(PESSIMISTIC_READ)
    @Query("SELECT b FROM EntityB b WHERE b.entityAId = :id")
    List<EntityB> findByEntityAIdWithLock(UUID id);
}
