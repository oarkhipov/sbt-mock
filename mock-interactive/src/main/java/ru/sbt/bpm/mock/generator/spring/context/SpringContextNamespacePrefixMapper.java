package ru.sbt.bpm.mock.generator.spring.context;


import java.util.HashMap;
import java.util.Map;

/**
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
//TODO make ns resolver
public class SpringContextNamespacePrefixMapper  {

    private Map<String, String> springContextNamespaces = new HashMap<String, String>(){{
        put("http://www.springframework.org/schema/beans","");
        put("http://www.springframework.org/schema/integration/jms","int-jms");
        put("http://www.springframework.org/schema/integration","int");
    }};

    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        return springContextNamespaces.containsKey(namespaceUri)?springContextNamespaces.get(namespaceUri):suggestion;
    }
}
