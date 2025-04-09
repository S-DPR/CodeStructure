package com.example.codestructure.extractor_sample.abstracts;


import com.example.codestructure.extractor_sample.dto.FormatPayload;
import com.example.codestructure.extractor_sample.entity.DataInfo;

public abstract class FileProcessor<T> {
    public abstract DataInfo process(FormatPayload payload);
    protected abstract String readAtPath(T root, String path);

    protected abstract String getNamePath();

    public abstract boolean canHandle(FormatPayload formatPayload);
}
