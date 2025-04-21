package com.example.codestructure.multi_format_parser;

import com.example.codestructure.multi_format_parser.dto.DataInfo;
import com.example.codestructure.multi_format_parser.dto.FormatPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataExtractionService {
    private final FormatPayloadFactory formatPayloadFactory;
    private final List<FileExtractor> extractors;

    public DataInfo getDataInfo(byte[] file) {
        FormatPayload payload = formatPayloadFactory.detect(file);
        FileExtractor fileExtractor = extractors.stream()
                .filter(extractor -> extractor.canHandle(payload))
                .findFirst()
                .orElse(null);
        if (fileExtractor == null) {
            log.warn("데이터 추출 가능한 파일이 아닙니다.");
            return new DataInfo();
        }
        return fileExtractor.process(payload);
    }
}
