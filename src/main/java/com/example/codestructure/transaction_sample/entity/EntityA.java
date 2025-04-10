package com.example.codestructure.transaction_sample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class EntityA {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    Integer data;
}
