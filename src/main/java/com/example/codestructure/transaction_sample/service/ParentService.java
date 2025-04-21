package com.example.codestructure.transaction_sample.service;

import com.example.codestructure.transaction_sample.entity.Parent;
import com.example.codestructure.transaction_sample.repos.ParentRepos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParentService {
    private final ParentRepos parentRepos;

    public ParentService(ParentRepos parentRepos) {
        this.parentRepos = parentRepos;
    }

    public Parent save(Parent parent) {
        return parentRepos.save(parent);
    }

    public Optional<Parent> findById(UUID id) {
        return parentRepos.findById(id);
    }

    public List<Parent> findAll() {
        return parentRepos.findAll();
    }

    public void deleteById(UUID id) {
        parentRepos.deleteById(id);
    }

    public Parent findOrSave(Parent parent) {
        return parentRepos.findById(parent.getId())
                .orElseGet(() -> save(parent));
    }
}
