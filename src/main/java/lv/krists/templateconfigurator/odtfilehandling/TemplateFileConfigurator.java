package lv.krists.templateconfigurator.odtfilehandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import lv.krists.templateconfigurator.entities.configurator.OdtConfiguration;
import lv.krists.templateconfigurator.odtfilehandling.properties.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class TemplateFileConfigurator {

    @Autowired
    private TemplateLinkReader linkReader;
    private final ConfigProperties properties;


    @Autowired
    public TemplateFileConfigurator(FileRegister fileRegister) {
        this.properties = fileRegister.getProperties();
    }

    //using configuration file, go though template files and replace with new imports
    public void processConfiguration() {
        OdtConfiguration odtConfiguration = findConfiguration();
        if(odtConfiguration !=null && !odtConfiguration.getLinkReplacements().isEmpty()) linkReader.replaceImportsInTemplates(odtConfiguration);
        log.info("Template configuration process finished");

    }

    //get OdtFileConfiguration from JSON file (file location specified in properties).
    private OdtConfiguration findConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        OdtConfiguration odtConfiguration = null;
        try {
            odtConfiguration = objectMapper.readValue(new File(properties.getOdtConfigurationJsonPath()), OdtConfiguration.class);
        } catch (MismatchedInputException e) {
            log.error("Configuration file cannot be deserialized to {}",OdtConfiguration.class);
        } catch (IOException e) {
            log.error("Configuration file not found at {}", properties.getOdtConfigurationJsonPath());
        }
        return odtConfiguration;
    }
}
