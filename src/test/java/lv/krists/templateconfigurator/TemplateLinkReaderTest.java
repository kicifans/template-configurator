package lv.krists.templateconfigurator;

import lv.krists.templateconfigurator.odtfilehandling.FileRegister;
import lv.krists.templateconfigurator.odtfilehandling.TemplateLinkReader;
import lv.krists.templateconfigurator.odtfilehandling.helper.OdtDomHelper;
import lv.krists.templateconfigurator.odtfilehandling.properties.ConfigProperties;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemplateLinkReaderTest {
    @Test
    void testSaveTemplateLinksJson() throws Exception {
        // create a temporary file path
        File tempFile = Files.createTempFile("template-links", ".json").toFile();
        tempFile.deleteOnExit(); // cleanup

        ConfigProperties props = new ConfigProperties();
        props.setLinksOutputPath(tempFile.getAbsolutePath());

        FileRegister fileRegister = new FileRegister(props);
        TemplateLinkReader reader = new TemplateLinkReader(fileRegister, new OdtDomHelper());

        //save json
        reader.saveTemplateLinksJson();

        //assert if it exists
        assertTrue(tempFile.exists(), "JSON file should be created");

        String content = Files.readString(tempFile.toPath());
        assertTrue(content.contains("templates"), "JSON should contain template info");
    }

    @Test
    void checkForTemplateLinks() throws IOException {
        // Create a temporary file to store the JSON output
        File tempOutput = File.createTempFile("links_", ".json");
        tempOutput.deleteOnExit(); // Clean up after the test

        ConfigProperties props = new ConfigProperties();
        //use test resources for example file
        props.setFilesLocationPath("src/test/resources/");
        props.setLinksOutputPath(tempOutput.getAbsolutePath());

        FileRegister fileRegister = new FileRegister(props);
        Path resourcePath = Paths.get("src", "test", "resources","template_test.odt");
        //put example block from template, to check if its found
        fileRegister.getTemplates().put("template_test", resourcePath);
        fileRegister.getInputTextDocuments().put("block_test.odt", resourcePath);
        TemplateLinkReader reader = new TemplateLinkReader(fileRegister, new OdtDomHelper());

        //read links from test file
        reader.processLinkReading();

        // Assert that the JSON file exists and has content
        assertFalse(reader.getTemplateImportLinks().getTemplates().isEmpty(), "Example block should be added to list, since it exists in template");
        assertTrue(tempOutput.exists(), "Output JSON file should be created");
        assertTrue(tempOutput.length() > 0, "Output JSON file should not be empty");
    }
}
