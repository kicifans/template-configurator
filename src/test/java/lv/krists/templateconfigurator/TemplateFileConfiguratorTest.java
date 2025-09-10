package lv.krists.templateconfigurator;

import lv.krists.templateconfigurator.entities.configurator.OdtConfiguration;
import lv.krists.templateconfigurator.odtfilehandling.FileRegister;
import lv.krists.templateconfigurator.odtfilehandling.TemplateFileConfigurator;
import lv.krists.templateconfigurator.odtfilehandling.properties.ConfigProperties;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class TemplateFileConfiguratorTest {
    @Test
    void testFindConfiguration() throws Exception {
        //temporary JSON file with sample configuration
        File tempFile = Files.createTempFile("block_replacement_configuration", ".json").toFile();
        String json = """
            {
                "linkReplacements": [
                    { "currentBlock": "block1", "newBlock": "block2" }
                ]
            }
            """;
        Files.writeString(tempFile.toPath(), json);

        ConfigProperties props = new ConfigProperties();
        props.setOdtConfigurationJsonPath(tempFile.getAbsolutePath());

        FileRegister fileRegister = new FileRegister(props);
        TemplateFileConfigurator configurator = new TemplateFileConfigurator(fileRegister);

        OdtConfiguration config = configurator.findConfiguration();

        assertNotNull(config, "Configuration should not be null");
        assertEquals("", 1, config.getLinkReplacements().size());
        assertEquals("","block1", config.getLinkReplacements().get(0).getCurrentBlock());
        assertEquals("","block2", config.getLinkReplacements().get(0).getNewBlock());
    }
}
