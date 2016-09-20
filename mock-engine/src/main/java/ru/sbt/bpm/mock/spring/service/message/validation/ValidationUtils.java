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

package ru.sbt.bpm.mock.spring.service.message.validation;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;
import ru.sbt.bpm.mock.spring.service.message.validation.exceptions.MockMessageValidationException;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author sbt-bochev-as on 10.03.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class ValidationUtils {
    public static String getSolidErrorMessage(List<MockMessageValidationException> exceptions) {
        if (exceptions.size() > 5) {
            exceptions = exceptions.subList(0, 4);
        }
        final String errorString = StringUtils.join(exceptions, ",\n");
        return "Validation errors list:\n" + errorString;
    }

    public static String getValidationHtmlErrorMessage(String message, List<MockMessageValidationException> exceptions) {
        String solidErrorMessage = getSolidErrorMessage(exceptions);

        String base64EncodedMessage = null;
        try {
            base64EncodedMessage = Base64Utils.encodeToString(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        String errorLinesString = FluentIterable.from(exceptions)
                .transform(new Function<MockMessageValidationException, Long>() {
                    @Override
                    public Long apply(MockMessageValidationException e) {
                        return e.getLineNumber();
                    }
                })
                .toString();

        return solidErrorMessage + "\n" +
                "<a href='#' onclick='showValidationErrorForm(\"" + base64EncodedMessage + "\",\"" + errorLinesString + "\");'>Details >></a>";
    }
}
