package ru.sbt.bpm.mock.generator.spring.context;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class SpringContextNamespacePrefixMapper extends NamespacePrefixMapper {

    private Map<String, String> springContextNamespaces = new HashMap<String, String>(){{
        put("http://www.springframework.org/schema/beans","");
        put("http://www.springframework.org/schema/integration/jms","int-jms");
        put("http://www.springframework.org/schema/integration","int");
    }};

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        return springContextNamespaces.containsKey(namespaceUri)?springContextNamespaces.get(namespaceUri):suggestion;
    }
}
