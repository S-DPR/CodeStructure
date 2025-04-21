package com.example.codestructure.transaction_sample.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Parent {
    @Id
    UUID id;
    Integer data;
}
