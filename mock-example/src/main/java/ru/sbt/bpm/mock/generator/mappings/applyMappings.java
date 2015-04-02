package ru.sbt.bpm.mock.generator.mappings;

import net.sf.saxon.lib.NamespaceConstant;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import ru.sbt.bpm.mock.config.entities.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by sbt-vostrikov-mi on 02.04.2015.
 */
public class applyMappings {
    private static applyMappings ourInstance = new applyMappings();

    public static applyMappings getInstance() {
        return ourInstance;
    }

    private applyMappings() {
    }

    private void applyMappedTags(File xml, MappedTagSequence mappedTags, Map<String, String> params) throws Exception  {
        if (mappedTags!=null && mappedTags.getListOfMappedTagTags()!= null && mappedTags.getListOfMappedTagTags().size()>0) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(FileUtils.readFileToByteArray(xml));
            Document xmlDoc = builder.parse(stream);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            String nameQuerry = "";
            if (params.containsKey("operationName")) {
                nameQuerry = " and @name='" + params.get("operationName") + "'";
            }
            XPathExpression findBaseElement = xpath.compile("//*[local-name()='template'][@name" + nameQuerry + "]/*[local-name()='element']");

            Node rootElement = (Node) findBaseElement.evaluate(xmlDoc, XPathConstants.NODE);

            for (MappedTag tagSq : mappedTags.getListOfMappedTagTags()) {
                if (tagSq.getMappedFromRqTags() != null) {
                    MappedFromRqTag tag = tagSq.getMappedFromRqTags();
                    Node element = findElementDescriptionInXSL(tag.getResponseTagSequence().getListOfLinkedTags(), rootElement);
                    element.setNodeValue(formElementDescription(tag.getRequestTagSequence().getListOfLinkedTags()));
                }
                if (tagSq.getXpathQuerrys() != null) {
                    MappedByXpath tag = tagSq.getXpathQuerrys();
                    Node element = findElementDescriptionInXSL(tag.getResponseTagSequence().getListOfLinkedTags(), rootElement);
                    element.setNodeValue(formElementDescription(tag.getQuerry()));
                }
            }
        }
    }

    private Node findElementDescriptionInXSL(List<LinkedTag> tags, Node rootElement) throws Exception {
        System.setProperty("javax.xml.xpath.XPathFactory:"+ NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl");
        XPathFactory xPathFactory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);

        //XPathFactory xPathFactory = net.sf.saxon.xpath.XPathFactoryImpl.newInstance();
        XPath xpath = xPathFactory.newXPath();

        Node element = rootElement;
        for (LinkedTag tag : tags) {
            String xPathQuerry = "/*[local-name()='"+tag.getTag() + "'] | " +
                    "if (/*[local-name()='apply-templates'][contains(@select,"+tag.getTag()+")]) then " +
                    "(../*[local-name()='template'][contains(@match,"+tag.getTag()+")])";
            XPathExpression findElement = xpath.compile(xPathQuerry);
            element = (Node) findElement.evaluate(element, XPathConstants.NODE);
        }
        return element;
    }

    private String formElementDescription(List<LinkedTag> tags) {
        return "test";//TODO not implemented
    }

    private String formElementDescription(String querry) {
        return "<xsl:value-of select=\""+querry+"\"/>";
    }


}
