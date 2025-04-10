package com.example.codestructure.file_processor_sample.dispatcher;

import com.example.codestructure.file_processor_sample.abstracts.FileProcessor;
import com.example.codestructure.file_processor_sample.dto.FormatPayload;
import com.example.codestructure.file_processor_sample.entity.DataInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

import java.util.List;

@Slf4j
@Component
public class Dispatcher {
    private final List<FileProcessor> strategies;
    private final FormatPayloadFactory payloadFactory;

    public Dispatcher(List<FileProcessor> fileProcessors, FormatPayloadFactory payloadFactory) {
        strategies = fileProcessors;
        this.payloadFactory = payloadFactory;
    }

    public DataInfo getDataInfo(byte[] file) {
        if (file == null) {
            log.warn("파일이 정상적으로 입력되지 않음");
            return new DataInfo();
        }

        FormatPayload support = payloadFactory.detect(file);
        FileProcessor extractor = strategies.stream()
                .filter(current -> current.canHandle(support))
                .findFirst()
                .orElse(null);
        if (extractor != null) {
            return extractor.process(support);
        }
        log.warn("추출 실패 : 지원 파일 여부 확인 필요");
        return new DataInfo();
    }
}
