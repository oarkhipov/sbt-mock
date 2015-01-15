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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 29.12.2014.
 */
public class Xsl20Transformer {



    public static String transform(String xsltFile, String xmlFile) throws TransformerException {
        return transform(xsltFile, xmlFile, null);
    }


    public static String transform(Resource xsltRes, Resource xmlRes) throws TransformerException, IOException {
        return transform(xsltRes, xmlRes, null, null);
    }

    public static String transform(Resource xsltRes, String xmlRes) throws TransformerException, IOException {
        return transform(xsltRes, xmlRes, null, null);
    }

    public static String transform(String xsltFile, String xmlFile, String paramName, String ParamValue) throws TransformerException {
        return transform(xsltFile, xmlFile, fetchParamsToMap(paramName,ParamValue));
    }


        /**
         * Отличается от остальных типов трансформа, что получает пути к файлам.
         * @param xsltFile путь к файлу xslt
         * @param xmlFile путь к файлу xml
         * @param params параметр
         * @return трансформированный xml
         * @throws TransformerException
         */
    public static String transform(String xsltFile, String xmlFile, Map<String, String> params) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);

        Source xslt = new StreamSource(new File(xsltFile));
        Transformer transformer = factory.newTransformer(xslt);

        if (params!=null && !params.isEmpty()) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                if(param.getKey() != null ) {
                    transformer.setParameter(param.getKey(), param.getValue());
                }
            }
        }

        Source xml = new StreamSource(new File(xmlFile));

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(xml, result);

        return writer.toString();
    }

    public static String transform(Resource xsltRes, Resource xmlRes, String paramName, String ParamValue) throws TransformerException, IOException {
        Source xslt = new StreamSource(xsltRes.getFile());
        Source xml = new StreamSource(xmlRes.getFile());

        return transform(xslt, xml, fetchParamsToMap(paramName, ParamValue));
    }

    public static String transform(Resource xsltRes, String xmlStr, Map<String, String> params) throws TransformerException, IOException {
        Source xslt = new StreamSource(xsltRes.getFile());
        Source xml = new StreamSource(new StringReader(xmlStr));

        return transform(xslt, xml, params);
    }

    public static String transform(Resource xsltRes, String xmlStr, String paramName, String ParamValue) throws TransformerException, IOException {
        Source xslt = new StreamSource(xsltRes.getFile());
        Source xml = new StreamSource(new StringReader(xmlStr));

        return transform(xslt, xml, fetchParamsToMap(paramName, ParamValue));
    }

    public static String transform(Source xslt, Source xml, String paramName, String ParamValue) throws TransformerException, IOException {
        return transform(xslt, xml, fetchParamsToMap(paramName, ParamValue));
    }

    public static String transform(Source xslt, Source xml, Map<String, String> params) throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);

        Transformer transformer = factory.newTransformer(xslt);

        if (params!=null && !params.isEmpty()) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                if(param.getKey() != null ) {
                    transformer.setParameter(param.getKey(), param.getValue());
                }
            }
        }

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(xml, result);

        return writer.toString();
    }

    private static Map<String, String> fetchParamsToMap(String name, String value) {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put(name, value);
        return params;
    }
}