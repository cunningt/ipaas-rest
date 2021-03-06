/**
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.ipaas.project.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.redhat.ipaas.connector.catalog.ConnectorCatalog;
import com.redhat.ipaas.connector.catalog.ConnectorCatalogProperties;
import com.redhat.ipaas.model.connection.Action;
import com.redhat.ipaas.model.connection.Connection;
import com.redhat.ipaas.model.integration.Integration;
import com.redhat.ipaas.model.integration.Step;

import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultIntegrationToProjectConverterTest {

    @Test
    public void testConvert() throws Exception {
        Assume.assumeFalse(isUrlEncoded(new File( "." ).getCanonicalPath()));
        Step step1 = new Step.Builder().stepKind("endpoint").connection(new Connection.Builder().configuredProperties("{}").build()).configuredProperties("{\"period\": \"5000\"}").action(new Action.Builder().camelConnectorPrefix("periodic-timer").camelConnectorGAV("com.redhat.ipaas:timer-connector:0.2.1").build()).build();
        Step step2 = new Step.Builder().stepKind("endpoint").connection(new Connection.Builder().configuredProperties("{}").build()).configuredProperties("{\"httpUri\": \"http://localhost:8080/hello\"}").action(new Action.Builder().camelConnectorPrefix("http-get").camelConnectorGAV("com.redhat.ipaas:http-get-connector:0.2.1").build()).build();
        Step step3 = new Step.Builder().stepKind("log").configuredProperties("{\"message\": \"Hello World! ${body}\"}").build();
        Step step4 = new Step.Builder().stepKind("endpoint").connection(new Connection.Builder().configuredProperties("{}").build()).configuredProperties("{\"httpUri\": \"http://localhost:8080/bye\"}").action(new Action.Builder().camelConnectorPrefix("http-post").camelConnectorGAV("com.redhat.ipaas:http-post-connector:0.2.1").build()).build();

        Map<String, byte[]> files = new DefaultIntegrationToProjectConverter(new ConnectorCatalog(new ConnectorCatalogProperties())).convert(
            new Integration.Builder()
                .id("test-integration")
                .name("Test Integration")
                .description("This is a test integration!")
                .gitRepo("https://ourgithhost.somewhere/test.git")
                .steps( Arrays.asList(step1, step2, step3, step4))
                .build()
        );
        assertFileContents(files.get("README.md"), "test-README.md");
        assertFileContents(files.get("src/main/java/com/redhat/ipaas/example/Application.java"), "test-Application.java");
        assertFileContents(files.get("src/main/resources/application.yml"), "test-application.yml");
        assertFileContents(files.get("src/main/resources/funktion.yml"), "test-funktion.yml");
        assertFileContents(files.get("pom.xml"), "test-pom.xml");
    }

    private void assertFileContents(byte[] actualContents, String expectedFileName) throws Exception {
        assertThat(new String(actualContents)).isEqualTo(
            new String(Files.readAllBytes(
                Paths.get(this.getClass().getResource(expectedFileName).toURI())
            ), StandardCharsets.UTF_8)
        );
    }

    @Test
    public void testConvertFromJson() throws Exception {
        Assume.assumeFalse(isUrlEncoded(new File( "." ).getCanonicalPath()));

        JsonNode json = new ObjectMapper().readTree(this.getClass().getResourceAsStream("test-integration.json"));
        Map<String, byte[]> files = new DefaultIntegrationToProjectConverter(new ConnectorCatalog(new ConnectorCatalogProperties())).convert(
            new ObjectMapper().registerModule(new Jdk8Module()).readValue(json.get("data").toString(), Integration.class)
        );
        assertFileContents(files.get("README.md"), "test-pull-push-README.md");
        assertFileContents(files.get("src/main/java/com/redhat/ipaas/example/Application.java"), "test-Application.java");
        assertFileContents(files.get("src/main/resources/application.yml"), "test-pull-push-application.yml");
        assertFileContents(files.get("src/main/resources/funktion.yml"), "test-pull-push-funktion.yml");
        assertFileContents(files.get("pom.xml"), "test-pull-push-pom.xml");
    }


    private static Boolean isUrlEncoded(String path) throws UnsupportedEncodingException {
        String encoded = URLDecoder.decode(path, "UTF-8");
        return !path.equals(encoded);

    }
}
