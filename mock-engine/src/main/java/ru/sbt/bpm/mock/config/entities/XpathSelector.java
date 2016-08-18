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

package ru.sbt.bpm.mock.config.entities;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbt-bochev-as
 *         01.12.2015.
 */
@Data
public class XpathSelector {

    @XStreamImplicit(itemFieldName = "tag")
    private List<ElementSelector> elementSelectors = new ArrayList<ElementSelector>();

    /**
     * конструктор
     */
    public XpathSelector() {
        elementSelectors = new ArrayList<ElementSelector>();
    }

    public XpathSelector(String[] namespaces, String[] names) {
        List<ElementSelector> elementSelectors = new ArrayList<ElementSelector>();
        for (int i = 0; i < names.length; i++) {
            ElementSelector elementSelector = new ElementSelector(
                    namespaces[i],
                    names[i]);
            elementSelectors.add(elementSelector);
        }
        this.elementSelectors = elementSelectors;
    }

    public String toXpath() {
        /*
        *   <beans:beanContainer id="operationSelector" class="java.lang.String">
        *       <beans:constructor-arg value="/*:Envelope[namespace-uri()='http://sbrf.ru/legal/enquiry/integration']/*[namespace-uri()='http://sbrf.ru/legal/enquiry/integration']"/>
        *   </beans:beanContainer>
        */
        StringBuilder stringBuilder = new StringBuilder();
        for (ElementSelector elementSelector : elementSelectors) {
            String element = elementSelector.getElement();
            String namespace = elementSelector.getNamespace();
            stringBuilder.append("/*[");
            if ((element != null) && (!element.isEmpty())) {
                stringBuilder.append("local-name()='")
                        .append(element)
                        .append("'");
            }
            if ((namespace != null) && (!namespace.isEmpty())) {
                if ((element != null) && (!element.isEmpty())) {
                    stringBuilder.append(" and ");
                }
                stringBuilder.append("namespace-uri()='")
                        .append(namespace)
                        .append("'");
            }
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}

