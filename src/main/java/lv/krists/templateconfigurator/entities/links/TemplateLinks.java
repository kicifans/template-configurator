package lv.krists.templateconfigurator.entities.links;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TemplateLinks {
    private ArrayList<Template> templates = new ArrayList<>();
}

