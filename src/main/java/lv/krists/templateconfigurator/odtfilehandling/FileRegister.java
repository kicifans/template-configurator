package lv.krists.templateconfigurator.odtfilehandling;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lv.krists.templateconfigurator.odtfilehandling.properties.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

@Component
@Slf4j
@Data
public class FileRegister {

    private final ConfigProperties properties;
    private final HashMap<String, Path> templates = new HashMap<>();
    private final HashMap<String, Path> inputTextDocuments = new HashMap<>();

    @Autowired
    public FileRegister(ConfigProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() throws IOException {
        //on app start go through specified properties path and go through all
        //folders and subfolders checking for template and block files, then register them
        //for easier access to names and location paths
        Path startPath = Paths.get(properties.getFilesLocationPath());
        try (Stream<Path> paths = Files.walk(startPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".odt"))
                    .forEach(odt -> {
                        String fileName = odt.getFileName().toString();
                        if (fileName.startsWith("template_")) {
                            templates.put(fileName, odt);
                            log.info("Template found {}", fileName);
                        } else if (isBlockFile(fileName)) {
                            inputTextDocuments.put(fileName, odt);
                            log.info("Block file found {}", fileName);
                        }
                    });
        } catch (NoSuchFileException e) {
            log.error("Incorrect location of ODT files specified! {}", startPath, e);
        }
    }

    private boolean isBlockFile(String name) {
        return name.startsWith("block_") || name.startsWith("footer_") ||
                name.startsWith("header_");
    }
}
