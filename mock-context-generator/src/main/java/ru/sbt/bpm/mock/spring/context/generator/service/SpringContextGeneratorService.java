/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.sbt.bpm.mock.spring.context.generator.service;

import generated.springframework.beans.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.spring.context.generator.CommentMarshalListener;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.BeansConstructor;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.IntegrationConstructor;
import ru.sbt.bpm.mock.spring.context.generator.service.constructors.JmsIntegrationConstructor;

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

	private Marshaller  marshaller;
	private Transformer transformer;

	@Autowired
	BeansConstructor beansConstructor;

	@Autowired
	IntegrationConstructor integrationConstructor;

	@Autowired
	JmsIntegrationConstructor jmsIntegrationConstructor;

	@PostConstruct
	private void init () throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(beansConstructor.getBeanFactory().getClass(),
		                                              integrationConstructor.getIntegrationFactory().getClass(),
		                                              jmsIntegrationConstructor.getJmsIntegrationFactory().getClass());
		marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.springframework.org/schema/beans        "
		                                                        + "http://www.springframework"
		                                                        + ".org/schema/beans/spring-beans.xsd\n" +
		                                                        "        http://www.springframework"
		                                                        + ".org/schema/integration http://www.springframework"
		                                                        + ".org/schema/integration/spring-integration.xsd\n" +
		                                                        "        http://www.springframework"
		                                                        + ".org/schema/integration/jms http://www"
		                                                        + ".springframework"
		                                                        + ".org/schema/integration/jms/spring-integration-jms"
		                                                        + ".xsd");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Unable to create Transformer!", e);
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	}

	public String toXml(Beans beans) throws JAXBException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);

		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter  xmlStreamWriter = null;
		try {
			xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(printStream);

			marshaller.setListener(new CommentMarshalListener(xmlStreamWriter));

			marshaller.marshal(beans, xmlStreamWriter);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			try {
				if (xmlStreamWriter != null) {
					xmlStreamWriter.close();
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

		String xml = "";
		//pretty Xml
		try {
			 xml = prettyXml(byteArrayOutputStream.toString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return xml;
	}

	public String prettyXml(String rawXml) {
		StreamResult result = new StreamResult(new StringWriter());
		rawXml = rawXml.replaceAll("&#xa;","");
		Source streamSource = new StreamSource(new StringReader(rawXml));
		try {
			transformer.transform(streamSource, result);
		} catch (TransformerException e) {
			throw new RuntimeException("Unable to prettify xml!",e);
		}
		return result.getWriter().toString();
	}
}
