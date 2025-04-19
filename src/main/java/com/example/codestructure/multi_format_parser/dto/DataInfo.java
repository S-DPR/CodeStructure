package com.example.codestructure.multi_format_parser.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataInfo {
    @Builder.Default
    private String name = "";
    @Builder.Default
    private String filename = "";
}
