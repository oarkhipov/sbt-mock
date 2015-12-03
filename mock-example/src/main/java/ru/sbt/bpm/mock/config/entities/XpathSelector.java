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

}

