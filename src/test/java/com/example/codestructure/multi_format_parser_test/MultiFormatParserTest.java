package com.example.codestructure.multi_format_parser_test;

import com.example.codestructure.multi_format_parser.DataExtractionService;
import com.example.codestructure.multi_format_parser.dto.DataInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.stream.Stream;

@SpringBootTest
public class MultiFormatParserTest {
    @Autowired
    DataExtractionService dataExtractionService;
    ClassLoader classLoader = getClass().getClassLoader();

    static Stream<Arguments> provideData() {
        return Stream.of(
                Arguments.of("ExampleA.xml", "EXPECTED_VALUE_A", "file_a.xml"),
                Arguments.of("ExampleB.xml", "EXPECTED_FIRSTNAME EXPECTED_LAST_NAME", "file_b.xml"),
                Arguments.of("ExampleC.json", "EXPECTED_VALUE_C", "file_c.json")
        );
    }

    @ParameterizedTest
    @MethodSource("provideData")
    void extractTest(String filename, String expectedName, String expectedFileName) throws IOException {
        byte[] bytes = classLoader.getResourceAsStream("multi_format_parser_test/" + filename).readAllBytes();
        DataInfo dataInfo = dataExtractionService.getDataInfo(bytes);
        Assertions.assertThat(dataInfo)
                .withFailMessage("DataInfo is null")
                .isNotNull();
        Assertions.assertThat(dataInfo.getName())
                .withFailMessage("Expected name not found : " + dataInfo.getName())
                .isEqualTo(expectedName);
        Assertions.assertThat(dataInfo.getFilename())
                .withFailMessage("Expected filename not found : " + dataInfo.getFilename())
                .isEqualTo(expectedFileName);
    }

    @Test
    void failTest() {
        byte[] bytes = new byte[] {'A', 'B', 'C'};
        DataInfo dataInfo = dataExtractionService.getDataInfo(bytes);
        Assertions.assertThat(dataInfo)
                .withFailMessage("DataInfo is null")
                .isNotNull();
        Assertions.assertThat(dataInfo.getName())
                .withFailMessage("Name should blank but name is : " + dataInfo.getName())
                .isEqualTo("");
        Assertions.assertThat(dataInfo.getFilename())
                .withFailMessage("Filename should blank but filename is : " + dataInfo.getFilename())
                .isEqualTo("");
    }
}
