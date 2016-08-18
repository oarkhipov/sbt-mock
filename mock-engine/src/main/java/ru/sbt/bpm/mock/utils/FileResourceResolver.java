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

package ru.sbt.bpm.mock.utils;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by sbt-vostrikov-mi on 03.04.2015.
 */
public class FileResourceResolver implements LSResourceResolver {

    /**
     * имплементация резолвера
     * пока делает только одну простую вещь - берет baseURI (адрес точки вызова), отризает от него путь, присоединяет к нему systemId(имя вызываемого файла). Создает объект, который возвращает потоки к этомук ресурсу.
     *
     * @param type         The type of the resource being resolved. For XML [XML 1.0] resources (i.e. entities), applications must use the value "http://www.w3.org/TR/REC-xml". For XML Schema [XML Schema Part 1] , applications must use the value"http://www.w3.org/2001/XMLSchema". Other types of resources are outside the scope of this specification and therefore should recommend an absolute URI in order to use this method.
     * @param namespaceURI namespaceURI - The namespace of the resource being resolved, e.g. the target namespace of the XML Schema [XML Schema Part 1] when resolving XML Schema resources.
     * @param publicId     The public identifier of the external entity being referenced, or null if no public identifier was supplied or if the resource is not an entity.
     * @param systemId     The system identifier, a URI reference [IETF RFC 2396], of the external resource being referenced, or null if no system identifier was supplied.
     * @param baseURI      The absolute base URI of the resource being parsed, or null if there is no base URI
     * @return object describing the new input source, or null to request that the parser open a regular URI connection to the resource.
     */
    public LSInput resolveResource(String type, String namespaceURI,
                                   String publicId, String systemId, String baseURI) {

        try {
            URI newUri = new URI(baseURI);
            String folder = FilenameUtils.getFullPath(newUri.getPath());
            String fileSubPath = systemId;
            if (systemId.startsWith("./")) {
                fileSubPath = systemId.replace("./", "");
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
