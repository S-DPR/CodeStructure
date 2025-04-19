package com.example.codestructure.multi_format_parser;

import com.example.codestructure.multi_format_parser.dto.DataInfo;
import com.example.codestructure.multi_format_parser.dto.FormatPayload;

public interface FileExtractor {
    DataInfo process(FormatPayload payload);

    // 제약 : 파일 하나를 모든 FileExtractor에 돌렸을 때, 2개 이상의 canHandle에서 true가 나오면 안됩니다.
    boolean canHandle(FormatPayload payload);
}
