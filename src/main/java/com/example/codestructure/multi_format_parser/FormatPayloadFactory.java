package com.example.codestructure.multi_format_parser;

import com.example.codestructure.multi_format_parser.dto.FormatPayload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class FormatPayloadFactory {
    public final ObjectMapper objectMapper;

    public FormatPayload detect(byte[] data) {
        return FormatPayload.builder()
                .xml(byteToXml(data))
                .json(byteToJson(data))
                .build();
    }

    public Node byteToXml(byte[] data) {
        // 샘플이므로 간단하게 구현
        if (data[0] != '<') return null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new ByteArrayInputStream(data)).getFirstChild();
        } catch (Exception e) {
            log.warn("XML 파싱 실패", e);
            return null;
        }
    }

    public JsonNode byteToJson(byte[] data) {
        // 샘플이므로 간단하게 구현
        if (!(data[0] == '[' || data[0] == '{')) return null;
        try {
            return objectMapper.readTree(new ByteArrayInputStream(data));
        } catch (Exception e) {
            log.warn("JSON 파싱 실패", e);
            return null;
        }
    }
}
