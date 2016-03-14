package ru.sbt.bpm.mock.spring.utils;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * @author sbt-bochev-as on 14.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class XpathUtils {
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
}
