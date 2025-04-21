package com.example.codestructure.multi_format_parser.readers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class JsonReader {

    // path는 /로 구분됩니다.
    public String readAtPath(JsonNode root, String path) {
        if (root == null || path == null || path.isBlank()) return null;

        JsonNode result = Arrays.stream(path.split("/"))
                .filter(p -> !p.isBlank())
                .reduce(root, JsonNode::path, (a, b) -> b);

        if (result == null || result.isMissingNode()) {
            log.error("JSON 경로 없음 : {}", path);
            return null;
        }
        return result.isValueNode() ? result.asText() : result.toString();
    }
}
