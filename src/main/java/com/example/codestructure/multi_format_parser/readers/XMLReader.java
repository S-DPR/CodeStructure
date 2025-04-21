package com.example.codestructure.multi_format_parser.readers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;

@Component
@Slf4j
public class XMLReader {

    // path는 /로 구분됩니다.
    public String readAtPath(Node root, String path) {
        if (root == null || path == null || path.isBlank()) return null;

        Node current = root;
        for (String part : path.split("/")) {
            if (part.isBlank()) continue;

            NodeList children = current.getChildNodes();
            current = Arrays.stream(toArray(children))
                            .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                            .filter(n -> part.equals(n.getNodeName()))
                            .findFirst()
                            .orElse(null);

            if (current == null) {
                log.error("XML 경로 에러 : {}", path);
                return null;
            }
        }

        return current.getTextContent().trim();
    }

    private Node[] toArray(NodeList nodeList) {
        Node[] arr = new Node[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            arr[i] = nodeList.item(i);
        }
        return arr;
    }
}
