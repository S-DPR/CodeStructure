package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.repos.EntityBRepos;
import org.springframework.stereotype.Service;

@Service
public class EntityBService {
    private final EntityBRepos entityBRepos;

    public EntityBService(EntityBRepos entityBRepos) {
        this.entityBRepos = entityBRepos;
    }
}
