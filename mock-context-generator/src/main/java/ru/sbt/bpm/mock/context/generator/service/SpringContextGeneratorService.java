package ru.sbt.bpm.mock.context.generator.service;

import generated.springframework.beans.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.IntegrationConstructor;
import ru.sbt.bpm.mock.context.generator.service.constructors.JmsIntegrationConstructor;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by sbt-hodakovskiy-da on 08.07.2016.
 */

@Service
public class SpringContextGeneratorService {

	private Marshaller marshaller;
	private Transformer transformer;

	@Autowired
	BeansConstructor beansConstructor;

	@Autowired
	IntegrationConstructor integrationConstructor;

	@Autowired
	JmsIntegrationConstructor jmsIntegrationConstructor;

	@PostConstruct
	private void init() throws JAXBException, TransformerConfigurationException {
		JAXBContext context = JAXBContext.newInstance(beansConstructor.getBeanFactory().getClass(), integrationConstructor.getIntegrationFactory().getClass(), jmsIntegrationConstructor.getJmsIntegrationFactory().getClass());
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,"http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration.xsd\n" +
		                                                       "        http://www.springframework.org/schema/integration/jms http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
//
//			// Spring integration namespace and prefix
//			private static final String INTEGRATION_PREFIX    = "int";
//			private static final String INTEGRATION_NAMESPACE = "http://www.springframework.org/schema/integration";
//
//			// Spring JMS integration namespace and prefix
//			private static final String JMS_INTEGRATION_PREFIX    = "jms";
//			private static final String JMS_INTEGRATION_NAMESPACE = "http://www.springframework.org/schema/integration/jms";
//
//			@Override
//			public String getPreferredPrefix (String namespace, String suggestion, boolean requiredPrefix) {
//				if (INTEGRATION_NAMESPACE.equals(namespace))
//					return INTEGRATION_PREFIX;
//				else if (JMS_INTEGRATION_NAMESPACE.equals(namespace))
//					return JMS_INTEGRATION_PREFIX;
//				return suggestion;
//			}
//
//			@Override
//			public String[] getPreDeclaredNamespaceUris () {
//				return new String[] { INTEGRATION_NAMESPACE, JMS_INTEGRATION_NAMESPACE };
//			}
//		});
//		marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new MockNamespaceMapper());

		transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	}

	public String toXml(Beans beans) throws XMLStreamException, JAXBException, UnsupportedEncodingException,
	                                        TransformerException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);

		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
		XMLStreamWriter  xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(printStream);

//		marshaller.setListener(new CommentMarshalListener(xmlStreamWriter));

		marshaller.marshal(beans, xmlStreamWriter);
		xmlStreamWriter.close();

		//pretty Xml
		return prettyXml(byteArrayOutputStream.toString("UTF-8"));
	}

	private String prettyXml(String rawXml) throws TransformerException {
		StreamResult result = new StreamResult(new StringWriter());
		Source streamSource = new StreamSource(new StringReader(rawXml));
		transformer.transform(streamSource, result);
		return result.getWriter().toString();
	}
}
