package com.example.codestructure.multi_format_parser.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.w3c.dom.Node;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class FormatPayload {
    private Node xml;
    private JsonNode json;

    public boolean hasXml() {
        return xml != null;
    }

    public boolean hasJson() {
        return json != null;
    }
}
