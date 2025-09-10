package lv.krists.templateconfigurator;

import lv.krists.templateconfigurator.odtfilehandling.FileRegister;
import lv.krists.templateconfigurator.odtfilehandling.properties.ConfigProperties;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileRegisterTest {
    @Test
    void testSaveTemplateLinksJson() throws Exception {
        // create a temporary file path
        File tempFile = Files.createTempFile("block_", ".odt").toFile();
        tempFile.deleteOnExit(); // cleanup

        ConfigProperties props = new ConfigProperties();
        props.setLinksOutputPath(tempFile.getAbsolutePath());

        FileRegister fileRegister = new FileRegister(props);

        assertTrue(fileRegister.isBlockFile(tempFile.getName()), "Should be registered as block file");
    }
}
