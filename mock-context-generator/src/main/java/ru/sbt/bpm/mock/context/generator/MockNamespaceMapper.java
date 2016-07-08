package ru.sbt.bpm.mock.context.generator;


import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Created by sbt-hodakovskiy-da on 07.07.2016.
 */
public class MockNamespaceMapper extends NamespacePrefixMapper {

	public static final String MAPPER_NAMESPACE_PROPERTY = "com.sun.xml.internal.bind.namespacePrefixMapper";

	// Spring integration namespace and prefix
	private static final String INTEGRATION_PREFIX    = "int";
	private static final String INTEGRATION_NAMESPACE = "http://www.springframework.org/schema/integration";

	// Spring JMS integration namespace and prefix
	private static final String JMS_INTEGRATION_PREFIX    = "jms";
	private static final String JMS_INTEGRATION_NAMESPACE = "http://www.springframework.org/schema/integration/jms";

	@Override
	public String getPreferredPrefix (String namespace, String suggestion, boolean requiredPrefix) {
		if (INTEGRATION_NAMESPACE.equals(namespace))
			return INTEGRATION_PREFIX;
		else if (JMS_INTEGRATION_NAMESPACE.equals(namespace))
			return JMS_INTEGRATION_PREFIX;
		return suggestion;
	}

	@Override
	public String[] getPreDeclaredNamespaceUris () {
		return new String[] { INTEGRATION_NAMESPACE, JMS_INTEGRATION_NAMESPACE };
	}
}
