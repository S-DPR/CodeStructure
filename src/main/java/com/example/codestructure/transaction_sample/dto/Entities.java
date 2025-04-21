package com.example.codestructure.transaction_sample.dto;

import com.example.codestructure.transaction_sample.entity.Parent;
import com.example.codestructure.transaction_sample.entity.Child;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Entities {
    private Parent parent;
    private Child child;
}
