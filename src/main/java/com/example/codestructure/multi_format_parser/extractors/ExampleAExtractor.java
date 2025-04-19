package com.example.codestructure.multi_format_parser.extractors;

import com.example.codestructure.multi_format_parser.FileExtractor;
import com.example.codestructure.multi_format_parser.dto.DataInfo;
import com.example.codestructure.multi_format_parser.dto.FormatPayload;
import com.example.codestructure.multi_format_parser.readers.XMLReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExampleAExtractor implements FileExtractor {
    private final XMLReader xmlReader;

    @Override
    public DataInfo process(FormatPayload payload) {
        try {
            Node root = payload.getXml();
            return DataInfo.builder()
                    .name(xmlReader.readAtPath(root, "NAME"))
                    .filename(xmlReader.readAtPath(root, "FILENAME"))
                    .build();
        } catch (Exception e) {
            log.error("ExampleA 파싱 중 알 수 없는 에러가 발생했습니다.\n{}", e.getMessage());
            return new DataInfo();
        }
    }

    @Override
    public boolean canHandle(FormatPayload payload) {
        if (!payload.hasXml()) return false;
        try {
            Node root = payload.getXml();
            return "EXPECTED_VALUE_A".equals(xmlReader.readAtPath(root, "NAME"));
        } catch (Exception e) {
            return false;
        }
    }
}
