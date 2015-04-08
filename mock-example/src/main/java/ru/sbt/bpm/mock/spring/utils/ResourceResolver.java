package ru.sbt.bpm.mock.spring.utils;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.*;
import java.net.URI;

/**
 * Created by sbt-vostrikov-mi on 03.04.2015.
 */
public class ResourceResolver implements LSResourceResolver {

    /**
     * имплементация резолвера
     * пока делает только одну простую вещь - берет baseURI (адрес точки вызова), отризает от него путь, присоединяет к нему systemId(имя вызываемого файла). Создает объект, который возвращает потоки к этомук ресурсу.
     * @param type The type of the resource being resolved. For XML [XML 1.0] resources (i.e. entities), applications must use the value "http://www.w3.org/TR/REC-xml". For XML Schema [XML Schema Part 1] , applications must use the value"http://www.w3.org/2001/XMLSchema". Other types of resources are outside the scope of this specification and therefore should recommend an absolute URI in order to use this method.
     * @param namespaceURI namespaceURI - The namespace of the resource being resolved, e.g. the target namespace of the XML Schema [XML Schema Part 1] when resolving XML Schema resources.
     * @param publicId The public identifier of the external entity being referenced, or null if no public identifier was supplied or if the resource is not an entity.
     * @param systemId The system identifier, a URI reference [IETF RFC 2396], of the external resource being referenced, or null if no system identifier was supplied.
     * @param baseURI The absolute base URI of the resource being parsed, or null if there is no base URI
     * @return object describing the new input source, or null to request that the parser open a regular URI connection to the resource.
     */
    public LSInput resolveResource(String type, String namespaceURI,
                                   String publicId, String systemId, String baseURI) {

        try {
            URI newUri = new URI(baseURI);
            String folder = FilenameUtils.getFullPath(newUri.getPath());
            String fileSubPath = systemId;
            if (systemId.startsWith("./")) {
                fileSubPath=systemId.replace("./", "");
            }
            File f = new File(folder + fileSubPath);
            InputStream resourceAsStream = new FileInputStream(f);
            return new Input(publicId, systemId, resourceAsStream, baseURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
