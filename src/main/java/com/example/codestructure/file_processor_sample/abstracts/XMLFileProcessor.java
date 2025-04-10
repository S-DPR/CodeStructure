package com.example.codestructure.file_processor_sample.abstracts;

import com.example.codestructure.file_processor_sample.dto.FormatPayload;
import org.w3c.dom.Node;

import java.util.List;

public abstract class XMLFileProcessor extends FileProcessor<Node> {
    @Override
    protected String readAtPath(Node root, String path) {
        List<String> paths = List.of(path.split("/"));
        Node currentNode = root;
        for(String next: paths) {
            currentNode = findNodeByName(currentNode, next, path);
        }
        return currentNode.getTextContent().trim();
    }

    private Node findNodeByName(Node root, String name, String path) {
        // MOCK: 실제 구현 생략
        return null;
    }

    protected abstract String getNamePath();

    public abstract boolean canHandle(FormatPayload payload);
}
