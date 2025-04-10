package com.example.codestructure.transaction_sample.dto;

import com.example.codestructure.transaction_sample.entity.EntityA;
import com.example.codestructure.transaction_sample.entity.EntityB;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Entities {
    private EntityA entityA;
    private EntityB entityB;
}
