package com.example.codestructure.transaction_sample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class EntityB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int fk;
    int data;
}
