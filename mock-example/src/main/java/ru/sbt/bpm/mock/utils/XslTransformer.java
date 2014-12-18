package ru.sbt.bpm.mock.utils;

import org.springframework.core.io.Resource;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by sbt-bochev-as on 13.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
public class XslTransformer {
    public static String transform(String xsltFile, String xmlFile) throws TransformerException {
        return transform(xsltFile, xmlFile, null, null);
    }

    public static String transform(Resource xsltRes, Resource xmlRes) throws TransformerException, IOException {
        return transform(xsltRes, xmlRes, null, null);
    }

    public static String transform(Resource xsltRes, String xmlRes) throws TransformerException, IOException {
        return transform(xsltRes, xmlRes, null, null);
    }

    public static String transform(String xsltFile, String xmlFile, String paramName, String ParamValue) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File(xsltFile));
        Transformer transformer = factory.newTransformer(xslt);

        if (paramName!=null && !paramName.isEmpty()) {
            transformer.setParameter(paramName, ParamValue);
        }

        Source xml = new StreamSource(new File(xmlFile));

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(xml, result);

        return writer.toString();
    }

    public static String transform(Resource xsltRes, Resource xmlRes, String paramName, String ParamValue) throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(xsltRes.getFile());
        Transformer transformer = factory.newTransformer(xslt);

        if (paramName!=null && !paramName.isEmpty()) {
            transformer.setParameter(paramName, ParamValue);
        }

        Source xml = new StreamSource(xmlRes.getFile());

        return transform(xslt, xml, paramName, ParamValue);
    }

    public static String transform(Resource xsltRes, String xmlStr, String paramName, String ParamValue) throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(xsltRes.getFile());
        Transformer transformer = factory.newTransformer(xslt);

        if (paramName!=null && !paramName.isEmpty()) {
            transformer.setParameter(paramName, ParamValue);
        }

        Source xml = new StreamSource(new StringReader(xmlStr));

        return transform(xslt, xml, paramName, ParamValue);
    }

    public static String transform(Source xslt, Source xml, String paramName, String ParamValue) throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer(xslt);

        if (paramName!=null && !paramName.isEmpty()) {
            transformer.setParameter(paramName, ParamValue);
        }

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(xml, result);

        return writer.toString();
    }
}
