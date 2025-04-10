package com.example.codestructure.file_processor_sample;

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

    public Dispatcher(List<FileProcessor> fileProcessors) {
        strategies = fileProcessors;
    }

    public DataInfo getDataInfo(byte[] file) {
        if (file == null) {
            log.warn("파일이 정상적으로 입력되지 않음");
            return new DataInfo();
        }

        FormatPayload support = getSupportFormat(file);
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

    private FormatPayload getSupportFormat(byte[] file) {
        FormatPayload formatPayload = new FormatPayload();
        try {
            Node xml = getNode(file);
            formatPayload.setXml(xml.getParentNode());
        } catch (Exception e) {
            log.info("XML 아님");
        }
        //...
        return formatPayload;
    }

    private Node getNode(byte[] file) throws Exception {
        // MOCK: 실제 구현 생략
        return null;
    }
}
