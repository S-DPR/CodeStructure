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
public class ExampleBExtractor implements FileExtractor {
    private final XMLReader xmlReader;

    @Override
    public DataInfo process(FormatPayload payload) {
        try {
            Node root = payload.getXml();

            String firstName = xmlReader.readAtPath(root, "PROPERTIES/FIRST_NAME");
            String lastName = xmlReader.readAtPath(root, "PROPERTIES/LAST_NAME");

            return DataInfo.builder()
                    .name(firstName + " " + lastName)
                    .filename(xmlReader.readAtPath(root, "PROPERTIES/FILENAME"))
                    .build();
        } catch (Exception e) {
            log.error("ExampleB 파싱 중 알 수 없는 에러가 발생했습니다.\n{}", e.getMessage());
            return new DataInfo();
        }
    }

    @Override
    public boolean canHandle(FormatPayload payload) {
        if (!payload.hasXml()) return false;
        try {
            Node root = payload.getXml();
            String firstName = xmlReader.readAtPath(root, "PROPERTIES/FIRST_NAME");
            String lastName = xmlReader.readAtPath(root, "PROPERTIES/LAST_NAME");
            return "EXPECTED_VALUE_B".equals(firstName+" "+lastName);
        } catch (Exception e) {
            return false;
        }
    }
}
