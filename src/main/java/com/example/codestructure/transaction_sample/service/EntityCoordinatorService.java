package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.dto.Entities;
import com.example.codestructure.transaction_sample.entity.EntityA;
import com.example.codestructure.transaction_sample.entity.EntityB;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EntityCoordinatorService {
    private final EntityAService entityAService;
    private final EntityBService entityBService;

    public EntityCoordinatorService(EntityAService entityAService, EntityBService entityBService) {
        this.entityAService = entityAService;
        this.entityBService = entityBService;
    }

    public Entities prepareEntitiesFKField(Entities entities) {
        EntityA entityA = entities.getEntityA();
        EntityB entityB = entities.getEntityB();

        UUID entityAId = entityA.getId();
        if (entityAId == null) {
            entityAId = UUID.randomUUID();
            entityA.setId(entityAId);
        }
        entityB.setEntityAId(entityAId);
        return entities;
    }

    @Transactional
    public Entities save(Entities entities) {
        entities = prepareEntitiesFKField(entities);
        EntityA entityA = entityAService.findOrSave(entities.getEntityA());
        EntityB entityB = entityBService.save(entities.getEntityB());
        return new Entities(entityA, entityB);
    }

    @Transactional
    public void delete(Entities entities) {
        entities = prepareEntitiesFKField(entities);

        UUID aId = entities.getEntityA().getId();
        UUID bId = entities.getEntityB().getId();

        EntityA lockedA = entityAService.findByIdWithLock(aId)
                .orElseThrow(() -> new IllegalStateException("EntityA not found"));
        entityBService.deleteById(bId);
        boolean stillUsed = entityBService.existsByEntityAId(lockedA.getId());
        if (!stillUsed) {
            entityAService.deleteById(aId);
        }
    }
}
