package com.example.codestructure.transaction_sample.repos;

import com.example.codestructure.transaction_sample.entity.EntityB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntityBRepos extends JpaRepository<EntityB, UUID> {
    boolean existsByEntityAId(UUID entityAId);
}
