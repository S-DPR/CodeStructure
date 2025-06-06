package com.example.codestructure.transaction_sample.repos;

import com.example.codestructure.transaction_sample.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChildRepos extends JpaRepository<Child, UUID> {
    boolean existsByParentId(UUID parentId);
}
