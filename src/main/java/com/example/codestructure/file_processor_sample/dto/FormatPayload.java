package com.example.codestructure.file_processor_sample.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.w3c.dom.Node;

@Getter
@Setter
@ToString
public class FormatPayload {
    Node xml;

    public boolean hasXml() {
        return xml != null;
    }
}
