package lv.krists.templateconfigurator.entities.configurator;

import lombok.Data;

import java.util.List;

@Data
public class OdtConfiguration {
    private List<LinkReplacement> linkReplacements;

    @Data
    public static class LinkReplacement {
        private String currentBlock;
        private String newBlock;
    }
}
