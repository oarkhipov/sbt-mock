package ru.sbt.bpm.mock.spring.context.generator.service;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Created by sbt-hodakovskiy-da on 14.07.2016.
 */

public class NamespaceMapper extends NamespacePrefixMapper {

	private static final String INT_PREFIX = "int";
	private static final String INT_URI = "http://www.springframework.org/schema/integration";

	private static final String JMS_PREFIX = "jms";
	private static final String JMS_URI = "http://www.springframework.org/schema/integration/jms";

	@Override
	public String getPreferredPrefix (String namespaceUri, String suggestion, boolean requirePrefix) {
		if (INT_URI.equals(namespaceUri))
			return INT_PREFIX;
		else if (JMS_URI.equals(namespaceUri))
			return JMS_PREFIX;
		return suggestion;
	}

	@Override
	public String[] getPreDeclaredNamespaceUris () {
		return new String[] { INT_URI, JMS_URI };
	}
}
