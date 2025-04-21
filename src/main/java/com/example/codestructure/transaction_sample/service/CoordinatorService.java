package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.dto.Entities;
import com.example.codestructure.transaction_sample.entity.Parent;
import com.example.codestructure.transaction_sample.entity.Child;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CoordinatorService {
    private final ParentService parentService;
    private final ChildService childService;

    public CoordinatorService(ParentService parentService, ChildService childService) {
        this.parentService = parentService;
        this.childService = childService;
    }

    public Entities prepareEntitiesFKField(Entities entities) {
        Parent parent = entities.getParent();
        Child child = entities.getChild();

        UUID parentId = parent.getId();
        if (parentId == null) {
            parentId = UUID.randomUUID();
            parent.setId(parentId);
        }
        child.setParentId(parentId);
        return entities;
    }

    @Transactional
    public Entities save(Entities entities) {
        entities = prepareEntitiesFKField(entities);
        Parent parent = parentService.findOrSave(entities.getParent());
        Child child = childService.save(entities.getChild());
        return new Entities(parent, child);
    }

    @Transactional
    public void delete(Entities entities) {
        entities = prepareEntitiesFKField(entities);
        childService.deleteById(entities.getChild().getId());
        boolean isUsing = childService.existsByEntityAId(entities.getChild().getParentId());
        if (!isUsing) {
            parentService.deleteById(entities.getParent().getId());
        }
    }
}
