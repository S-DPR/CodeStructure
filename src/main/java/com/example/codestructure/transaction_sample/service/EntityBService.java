package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.entity.EntityB;
import com.example.codestructure.transaction_sample.repos.EntityBRepos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EntityBService {
    private final EntityBRepos entityBRepos;

    public EntityBService(EntityBRepos entityBRepos) {
        this.entityBRepos = entityBRepos;
    }

    public EntityB save(EntityB entityB) {
        return entityBRepos.save(entityB);
    }

    public Optional<EntityB> findById(UUID id) {
        return entityBRepos.findById(id);
    }

    public List<EntityB> findAll() {
        return entityBRepos.findAll();
    }

    public void deleteById(UUID id) {
        entityBRepos.deleteById(id);
    }

    public boolean existsByEntityAId(UUID entityAId) {
        return entityBRepos.existsByEntityAId(entityAId);
    }
}
