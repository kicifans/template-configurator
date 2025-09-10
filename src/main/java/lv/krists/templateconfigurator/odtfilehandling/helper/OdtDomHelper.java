package lv.krists.templateconfigurator.odtfilehandling.helper;

import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.function.Consumer;

@Component
public class OdtDomHelper {
    // search for paragraphs contents
    public void scanParagraphs(NodeList paragraphs, Consumer<Element> consumer) {
        for (int i = 0; i < paragraphs.getLength(); i++) {
            Node node = paragraphs.item(i);
            if (node instanceof TextPElement p) {
                NodeList inputs = p.getElementsByTagName("text:text-input");
                for (int j = 0; j < inputs.getLength(); j++) {
                    Node inputNode = inputs.item(j);
                    if (inputNode instanceof Element input) {
                        consumer.accept(input);
                    }
                }
            }
        }
    }

    // go through header and footers (styles.xml)
    public void scanHeaderFooter(NodeList sections, Consumer<Element> consumer) {
        for (int i = 0; i < sections.getLength(); i++) {
            Element section = (Element) sections.item(i);
            NodeList paras = section.getElementsByTagName("text:p");
            scanParagraphs(paras, consumer);
        }
    }

    // strip import string to get filename
    public String clearImportMask(String value) {
        if (value == null) return "";
        return value.replaceFirst("^\\[import\\s+([^]]+)]$", "$1");
    }
}
