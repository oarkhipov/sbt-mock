package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as
 * 01.12.2015.
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

    public String toXpath() {
        /*
        *   <beans:bean id="operationSelector" class="java.lang.String">
        *       <beans:constructor-arg value="/*:Envelope[namespace-uri()='http://sbrf.ru/legal/enquiry/integration']/*[namespace-uri()='http://sbrf.ru/legal/enquiry/integration']"/>
        *   </beans:bean>
        */
        StringBuilder stringBuilder = new StringBuilder();
        for (ElementSelector elementSelector : elementSelectors) {
            String element = elementSelector.getElement();
            String namespace = elementSelector.getNamespace();
            stringBuilder.append("/*[");
            if((element != null) && (!element.isEmpty())) {
                stringBuilder.append("local-name()='")
                        .append(element)
                        .append("'");
            }
            if ((namespace != null) && (!namespace.isEmpty())) {
                if((element != null) && (!element.isEmpty())) {
                    stringBuilder.append(" and ");
                }
                stringBuilder.append("namespace-uri()='")
                        .append(namespace)
                        .append("'");
            }
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}

