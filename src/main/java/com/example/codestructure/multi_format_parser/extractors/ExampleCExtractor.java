package com.example.codestructure.multi_format_parser.extractors;

import com.example.codestructure.multi_format_parser.FileExtractor;
import com.example.codestructure.multi_format_parser.dto.DataInfo;
import com.example.codestructure.multi_format_parser.dto.FormatPayload;
import com.example.codestructure.multi_format_parser.readers.JsonReader;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExampleCExtractor implements FileExtractor {
    private final JsonReader jsonReader;

    @Override
    public DataInfo process(FormatPayload payload) {
        try {
            JsonNode root = payload.getJson();
            return DataInfo.builder()
                    .name(jsonReader.readAtPath(root, "NAME"))
                    .filename(jsonReader.readAtPath(root, "FILENAME"))
                    .build();
        } catch (Exception e) {
            log.error("ExampleC 파싱 중 알 수 없는 에러가 발생했습니다.\n{}", e.getMessage());
            return new DataInfo();
        }
    }

    @Override
    public boolean canHandle(FormatPayload payload) {
        if (!payload.hasJson()) return false;
        try {
            JsonNode root = payload.getJson();
            return "EXPECTED_VALUE_C".equals(jsonReader.readAtPath(root, "NAME"));
        } catch (Exception e) {
            return false;
        }
    }
}
