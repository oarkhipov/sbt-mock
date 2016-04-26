package ru.sbt.bpm.mock.spring.service;

import org.springframework.stereotype.Service;
import ru.sbt.bpm.mock.generator.spring.context.CommentMarshalListener;
import ru.sbt.bpm.mock.generator.spring.context.bean.Bean;
import ru.sbt.bpm.mock.generator.spring.context.bean.BeanContainer;

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
 * @author sbt-bochev-as on 13.01.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Service
public class SpringContextGeneratorService {

    private Marshaller marshaller;
    private Transformer transformer;

    @PostConstruct
    private void init() throws JAXBException, TransformerConfigurationException {
        JAXBContext context = JAXBContext.newInstance(BeanContainer.class, Bean.class);
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    public String toXml(BeanContainer beanContainer) throws UnsupportedEncodingException, TransformerException, XMLStreamException, JAXBException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(printStream);

        marshaller.setListener(new CommentMarshalListener(xmlStreamWriter));

        marshaller.marshal(beanContainer, xmlStreamWriter);
        xmlStreamWriter.close();

        //pretty Xml
        return prettyXml(byteArrayOutputStream.toString("UTF-8"));
//        return byteArrayOutputStream.toString();
    }

    private String prettyXml(String rawXml) throws TransformerException {
        StreamResult result = new StreamResult(new StringWriter());
        Source streamSource = new StreamSource(new StringReader(rawXml));
        transformer.transform(streamSource, result);
        return result.getWriter().toString();
    }
}
