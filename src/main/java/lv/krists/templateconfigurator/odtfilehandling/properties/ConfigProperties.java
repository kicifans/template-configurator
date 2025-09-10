package lv.krists.templateconfigurator.odtfilehandling.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "template.configuration")
public class ConfigProperties {
    String filesLocationPath;
    String odtConfigurationJsonPath;
    String linksOutputPath;
}
