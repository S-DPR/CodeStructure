package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.entity.EntityA;
import com.example.codestructure.transaction_sample.repos.EntityARepos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EntityAService {
    private final EntityARepos entityARepos;

    public EntityAService(EntityARepos entityARepos) {
        this.entityARepos = entityARepos;
    }

    public EntityA save(EntityA entityA) {
        return entityARepos.save(entityA);
    }

    public Optional<EntityA> findById(UUID id) {
        return entityARepos.findById(id);
    }

    public List<EntityA> findAll() {
        return entityARepos.findAll();
    }

    public void deleteById(UUID id) {
        entityARepos.deleteById(id);
    }

    public Optional<EntityA> findByIdWithLock(UUID id) {
        return entityARepos.findByIdWithLock(id);
    }

    @Transactional
    public EntityA findOrSave(EntityA entityA) {
        UUID id = entityA.getId();
        if (id == null) {
            throw new IllegalArgumentException("EntityA ID must not be null.");
        }
        return entityARepos.findByIdWithLock(id)
                .orElseGet(() -> entityARepos.save(entityA));
    }
}
