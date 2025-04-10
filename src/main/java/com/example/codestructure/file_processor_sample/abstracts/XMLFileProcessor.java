package com.example.codestructure.file_processor_sample.abstracts;

import com.example.codestructure.file_processor_sample.dto.FormatPayload;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        if (root == null) {
            throw new IllegalArgumentException("[" + path + "] 중간 노드가 null임");
        }
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals(name)) {
                return child;
            }
        }
        throw new IllegalArgumentException("경로 [" + path + "] 에 해당하는 노드 없음: " + name);
    }

    protected abstract String getNamePath();

    public abstract boolean canHandle(FormatPayload payload);
}
