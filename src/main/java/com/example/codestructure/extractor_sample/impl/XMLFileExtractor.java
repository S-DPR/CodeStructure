package com.example.codestructure.extractor_sample.impl;

import com.example.codestructure.extractor_sample.abstracts.XMLFileProcessor;
import com.example.codestructure.extractor_sample.dto.FormatPayload;
import com.example.codestructure.extractor_sample.entity.DataInfo;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;

// Path is Landsat8 Sample Data
@Component
public class XMLFileExtractor extends XMLFileProcessor {
    @Override
    public DataInfo process(FormatPayload payload) {
        Node root = payload.getXml();

        DataInfo dataInfo = new DataInfo();
        dataInfo.setName(getNamePath());
        dataInfo.setFilename(readAtPath(root, "PRODUCT_CONTENTS/LANDSAT_PRODUCT_ID"));

        return dataInfo;
    }

    @Override
    protected String getNamePath() {
        return "IMAGE_ATTRIBUTES/SPACECRAFT_ID";
    }

    @Override
    public boolean canHandle(FormatPayload formatPayload) {
        if (!formatPayload.hasXml()) return false;
        try {
            String name = readAtPath(formatPayload.getXml(), getNamePath());
            return "EXPECTED_SATELLITE_NAME".equals(name);
        } catch (Exception e) {
            return false;
        }
    }
}
