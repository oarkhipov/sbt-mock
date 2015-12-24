package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbt-bochev-as on 01.12.2015.
 */
@Data
public class XpathSelector {

    @XStreamImplicit(itemFieldName = "tag")
    private List<ElementSelector> elementSelectors = new ArrayList<ElementSelector>();

    /**
     * конструктор
     */
    public XpathSelector() {
        elementSelectors = new ArrayList<ElementSelector>();
    }

    public XpathSelector(String[] namespaces, String[] names) {
        List<ElementSelector> elementSelectors = new ArrayList<ElementSelector>();
        for (int i = 0; i < names.length; i++) {
            ElementSelector elementSelector = new ElementSelector(
                    namespaces[i],
                    names[i]);
            elementSelectors.add(elementSelector);
        }
        this.elementSelectors = elementSelectors;
    }

}

