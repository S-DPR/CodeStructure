package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.entity.EntityA;
import com.example.codestructure.transaction_sample.repos.EntityARepos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntityAService {
    private final EntityARepos entityARepos;

    public EntityAService(EntityARepos entityARepos) {
        this.entityARepos = entityARepos;
    }

    public EntityA save(EntityA entityA) {
        return entityARepos.save(entityA);
    }

    public Optional<EntityA> findById(int id) {
        return entityARepos.findById(id);
    }

    public List<EntityA> findAll() {
        return entityARepos.findAll();
    }
}
