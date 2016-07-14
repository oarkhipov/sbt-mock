package ru.sbt.bpm.mock.spring.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author sbt-bochev-as on 14.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */

@Slf4j
public class XmlUtils {
    private static Transformer transformer;
    static {
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    public static XdmValue evaluateXpath(String inputXml, String xpath) throws SaxonApiException {
        Processor processor = new Processor(false);
        XPathCompiler xPathCompiler = processor.newXPathCompiler();
        DocumentBuilder builder = processor.newDocumentBuilder();

        //build doc
        StringReader reader = new StringReader(inputXml);
        XdmNode xdmNode = builder.build(new StreamSource(reader));

        //load xpath
        XPathSelector selector = xPathCompiler.compile(xpath).load();
        selector.setContextItem(xdmNode);
        return selector.evaluate();
    }

    public static String compactXml(String xml) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(xml));
        String line;
        StringWriter stringWriter = new StringWriter();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringWriter.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        xml = stringWriter.toString();
        log.debug("Parsed xml: " + xml);
        return xml;
    }

    public static String prettyXml(String rawXml) {
        StreamResult result = new StreamResult(new StringWriter());
        Source streamSource = new StreamSource(new StringReader(rawXml));
        try {
            transformer.transform(streamSource, result);
        } catch (TransformerException e) {
            throw new RuntimeException("Unable to prettify xml!",e);
        }
        return result.getWriter().toString();
    }
}
