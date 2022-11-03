package ru.netology;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;
import java.util.Objects;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudStorageApplicationTestcontainersTest {
    private static final String HOST = "http://localhost:";
    private static final int PORT = 9090;

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    public static final GenericContainer<?> container = new GenericContainer<>(new ImageFromDockerfile()
            .withFileFromPath(".", Paths.get("target"))
            .withDockerfileFromBuilder(builder -> builder
                    .from("openjdk:17-alpine")
                    .expose(PORT)
                    .add("CloudStorage-0.0.1-SNAPSHOT.jar", "CloudStorage.jar")
                    .entryPoint("java", "-jar", "CloudStorage.jar")  // "/CloudStorage.jar"
                    .build()))
            .withExposedPorts(PORT);

//    @Test
//    void contextLoadsTransfer() throws JSONException {
//        ResponseEntity<Object> forTransfer = restTemplate.postForEntity(HOST + container.getMappedPort(PORT) +
//                ENDPOINT_TRANSFER, TRANSFER_REQUEST, Object.class);
//        String expected = new JSONObject(Objects.requireNonNull(forTransfer.getBody()).toString())
//                .get("operationId").toString();
//        Assertions.assertEquals(expected, OPERATION_ID);
//    }
//
//    @Test
//    void contextLoadsConfirmOperation() throws JSONException {
//        ResponseEntity<Object> forConfirmOperation = restTemplate.postForEntity(HOST + container.getMappedPort(PORT) +
//                ENDPOINT_CONFIRM, CONFIRM_OPERATION_REQUEST, Object.class);
//        String expected = new JSONObject(Objects.requireNonNull(forConfirmOperation.getBody()).toString())
//                .get("operationId").toString();
//        Assertions.assertEquals(expected, OPERATION_ID);
//    }

}