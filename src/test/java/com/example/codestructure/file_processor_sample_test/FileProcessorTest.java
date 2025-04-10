package com.example.codestructure.file_processor_sample_test;

import com.example.codestructure.file_processor_sample.dispatcher.Dispatcher;
import com.example.codestructure.file_processor_sample.entity.DataInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@SpringBootTest
public class FileProcessorTest {
    @Autowired Dispatcher dispatcher;

    public MockMultipartFile filePathToFile(String filepath) throws IOException {
        Path filePath = Path.of(filepath);
        byte[] fileContent = Files.readAllBytes(filePath);
        String fileName = filePath.getFileName().toString();
        String contentType = Files.probeContentType(filePath);

        return new MockMultipartFile(
                "file",
                fileName,
                contentType,
                fileContent
        );
    }

    static Stream<Arguments> fileProvider() {
        return Stream.of(
                Arguments.of("sample/File/Path", "EXPECTED_NAME", "EXPECTED_FILENAME")
        );
    }

    @ParameterizedTest
    @MethodSource("fileProvider")
    public void metadataTest(String dataPath, String expectedName, String expectedFilename) throws IOException {
        DataInfo dataInfo = dispatcher.getDataInfo(filePathToFile(dataPath).getBytes());
        Assertions.assertThat(dataInfo).isNotNull();
        Assertions.assertThat(dataInfo.getName()).isEqualTo(expectedName);
        Assertions.assertThat(dataInfo.getFilename()).isEqualTo(expectedFilename);
    }
}
