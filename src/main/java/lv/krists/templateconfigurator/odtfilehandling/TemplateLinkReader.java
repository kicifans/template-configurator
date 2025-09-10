package lv.krists.templateconfigurator.odtfilehandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lv.krists.templateconfigurator.entities.configurator.OdtConfiguration;
import lv.krists.templateconfigurator.entities.links.Template;
import lv.krists.templateconfigurator.entities.links.TemplateLinks;
import lv.krists.templateconfigurator.odtfilehandling.helper.OdtDomHelper;
import lv.krists.templateconfigurator.odtfilehandling.properties.ConfigProperties;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.OdfStylesDom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TemplateLinkReader {

    private final FileRegister fileRegister;
    private final ConfigProperties properties;
    private final OdtDomHelper domHelper;
    private final TemplateLinks templateImportLinks = new TemplateLinks();

    @Autowired
    public TemplateLinkReader(FileRegister fileRegister, OdtDomHelper domHelper) {
        this.fileRegister = fileRegister;
        this.properties = fileRegister.getProperties();
        this.domHelper = domHelper;
    }

    //read templates for links
    public void processLinkReading() {
        searchForTemplateLinks();
        log.info("Template processing finished.");
    }

    // load previously found template files and
    // search for import mentions of previously found block/header/footer files
    private void searchForTemplateLinks() {
        fileRegister.getTemplates().forEach((name, path) -> {
            Template template = new Template(name);
            File file = new File(path.toUri());


            processOdtFile(file, input -> {
                //check if any of block/header/footer files mentioned in template
                String value = input.getTextContent();
                if (value.startsWith("[import") && fileRegister.getInputTextDocuments().containsKey(domHelper.clearImportMask(value))) {
                    log.info("Template {} contains block file link {} {}", template.getTemplateName(), value, file.getAbsolutePath());
                    template.getReference().add(value);
                }
            });
            //if template contains any import links, add them to list
            if (!template.getReference().isEmpty()) {
                templateImportLinks.getTemplates().add(template);
            }
        });

        //creates new Json file at specified location, containing information about links attached to it
        saveTemplateLinksJson();
    }

    //generic ODT file processing
    private void processOdtFile(File file, Consumer<Element> elementConsumer) {
        try {
            OdfTextDocument document = OdfTextDocument.loadDocument(file);

            //scan body paragraphs
            NodeList bodyParas = document.getContentRoot().getElementsByTagName("text:p");
            domHelper.scanParagraphs(bodyParas, elementConsumer);

            //scan headers and footers
            OdfStylesDom styles = document.getStylesDom();
            NodeList masterPages = styles.getElementsByTagName("style:master-page");
            for (int i = 0; i < masterPages.getLength(); i++) {
                Node masterPage = masterPages.item(i);
                if (masterPage instanceof Element page) {
                    domHelper.scanHeaderFooter(page.getElementsByTagName("style:header"), elementConsumer);
                    domHelper.scanHeaderFooter(page.getElementsByTagName("style:footer"), elementConsumer);
                }
            }
            //save changes if elementConsumer modified document
            document.save(file);

        } catch (Exception e) {
            log.error("Failed to process ODT file {}", file.getAbsolutePath(), e);
        }
    }

    //check configuration against all templates. If template contains import from configuration,
    //replace its current value to new one
    public void replaceImportsInTemplates(OdtConfiguration configuration) {
        Map<String, String> replacementMap = configuration.getLinkReplacements().stream()
                .collect(Collectors.toMap(OdtConfiguration.LinkReplacement::getCurrentBlock,
                        OdtConfiguration.LinkReplacement::getNewBlock));

        fileRegister.getTemplates().forEach((name, path) -> {
            File file = new File(path.toUri());

            //use generic ODT processing helper
            processOdtFile(file, input -> {
                String value = input.getTextContent();
                String clean = domHelper.clearImportMask(value);
                if (value.startsWith("[import") && replacementMap.containsKey(clean)) {
                    String newBlock = replacementMap.get(clean);
                    String newValue = "[import " + newBlock + "]";
                    input.setTextContent(newValue);
                    log.info("Replaced import '{}' → '{}'", value, newValue);
                }
            });

            log.info("Replacements applied for template {}", name);
        });
    }

    //creates new Json file at specified location, containing information about links attached to it
    private void saveTemplateLinksJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(properties.getLinksOutputPath()), templateImportLinks);
        } catch (IOException e) {
            log.error("Error while saving template imports file! ", e);
        }
    }
}
