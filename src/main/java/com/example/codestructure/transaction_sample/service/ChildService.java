package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.entity.Child;
import com.example.codestructure.transaction_sample.repos.ChildRepos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChildService {
    private final ChildRepos childRepos;

    public ChildService(ChildRepos childRepos) {
        this.childRepos = childRepos;
    }

    public Child save(Child child) {
        return childRepos.save(child);
    }

    public Optional<Child> findById(UUID id) {
        return childRepos.findById(id);
    }

    public List<Child> findAll() {
        return childRepos.findAll();
    }

    public void deleteById(UUID id) {
        childRepos.deleteById(id);
    }

    public boolean existsByEntityAId(UUID entityAId) {
        return childRepos.existsByParentId(entityAId);
    }
}
