package lv.krists.templateconfigurator.entities.links;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Template {
    private String templateName;
    private ArrayList<String> reference = new ArrayList<>();

    public Template(String templateName) {
        this.templateName = templateName;
    }
}
