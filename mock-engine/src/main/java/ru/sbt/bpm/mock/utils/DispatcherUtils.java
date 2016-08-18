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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmValue;
import ru.sbt.bpm.mock.config.enums.DispatcherTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbt-bochev-as on 04.08.2016.
 *         <p>
 *         Company: SBT - Moscow
 */

@Slf4j
public class DispatcherUtils {

    public static boolean check(String payload, DispatcherTypes dispatcherType, String dispatcherExpression, String regexGroups, String value) {
        if (dispatcherType.equals(DispatcherTypes.XPATH)) return checkXpath(payload, dispatcherExpression, value);
        if (dispatcherType.equals(DispatcherTypes.REGEX))
            return checkRegex(payload, dispatcherExpression, regexGroups, value);
        if (dispatcherType.equals(DispatcherTypes.GROOVY)) return checkGroovy(payload, dispatcherExpression, value);
        return false;
    }

    private static boolean checkXpath(String payload, String dispatcherExpression, String value) {
        try {
            XdmValue xdmValue =  XmlUtils.evaluateXpath(XmlUtils.compactXml(payload), dispatcherExpression).itemAt(0);
            return xdmValue.toString().equals(value);
        } catch (SaxonApiException e) {
            log.error("Exception at XPATH dispatcher process!", e);
            return false;
        }
    }

    private static boolean checkRegex(String payload, String dispatcherExpression, String regexGroups, String value) {

        Pattern groups = Pattern.compile("\\$(\\d+)");
        Matcher groupsMatcher = groups.matcher(regexGroups);
        List<Integer> allWantedGroups = new ArrayList<Integer>();
        while (groupsMatcher.find()) {
            allWantedGroups.add(Integer.valueOf(groupsMatcher.group(1)));
        }

        Pattern dispatcherRegexPattern = Pattern.compile(dispatcherExpression);
        Matcher dispatcherMatcher = dispatcherRegexPattern.matcher(payload);
        List<String> groupValues = new ArrayList<String>();
        if (dispatcherMatcher.find()) {

            for (Integer wantedGroup : allWantedGroups) {
                groupValues.add(dispatcherMatcher.group(wantedGroup));
            }

            String format = regexGroups.replaceAll("\\$\\d+", "\\%s");
            return String.format(format, groupValues.toArray()).equals(value);
        } else {
            return false;
        }
    }

    private static boolean checkGroovy(final String payload, String dispatcherExpression, String value) {
        Binding binding = new Binding(new HashMap<String, String>(){{
            put("request",payload);
        }});
        GroovyShell groovyShell = new GroovyShell(binding);
        String groovyResult = String.valueOf(groovyShell.evaluate("def requestDom = new XmlParser().parseText(request);\n" + dispatcherExpression));
        return groovyResult.equals(value);
    }


}
