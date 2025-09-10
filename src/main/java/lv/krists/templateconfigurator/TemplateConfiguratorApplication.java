package lv.krists.templateconfigurator;

import lv.krists.templateconfigurator.odtfilehandling.TemplateFileConfigurator;
import lv.krists.templateconfigurator.odtfilehandling.TemplateLinkReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TemplateConfiguratorApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TemplateConfiguratorApplication.class, args);
        TemplateFileConfigurator fileConfigurator = context.getBean(TemplateFileConfigurator.class);
        fileConfigurator.processConfiguration();
        TemplateLinkReader reader = context.getBean(TemplateLinkReader.class);
        reader.processLinkReading();
	}

}
