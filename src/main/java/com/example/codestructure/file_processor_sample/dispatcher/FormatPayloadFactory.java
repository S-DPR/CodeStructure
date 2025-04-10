package com.example.codestructure.file_processor_sample.dispatcher;

import com.example.codestructure.file_processor_sample.dto.FormatPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@Slf4j
@Component
public class FormatPayloadFactory {
    public FormatPayload detect(byte[] file) {
        // 파일이 입력되지 않았으면 모두 null이 들어간 FormatPayload 반환
        if (file == null || file.length == 0) {
            log.warn("파일 입력되지 않음");
            return new FormatPayload();
        }
        FormatPayload payload = new FormatPayload();

        // 샘플이므로 간단하게 체크
        if (file[0] == '<') {
            try {
                Node root = getNode(file);
                payload.setXml(root);
            } catch (Exception e) {
                log.warn("XML 파싱 실패");
            }
        }
        return payload;
    }

    // 아래는 단순화된 예시이며, 실제 운영에서는 다른 구현을 사용합니다.
    private Node getNode(byte[] file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(file);
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        return document.getDocumentElement(); // 루트 노드 반환
    }
}
