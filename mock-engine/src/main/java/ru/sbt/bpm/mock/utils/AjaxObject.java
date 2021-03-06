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

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by sbt-bochev-as on 17.12.2014.
 * <p/>
 * Company: SBT - Moscow
 */
@AllArgsConstructor
public class AjaxObject {
    private transient Gson gson = new Gson();
    @Getter private String info;
    @Getter private String error;
    @Getter private Object data;

    public AjaxObject() {
        this.info = "";
        this.error = "";
        this.data = "";
    }

    public AjaxObject(String info, Exception e, String data) {
        this.info = info;
        setError(e);
        this.data = data;
    }

    public void setInfo(String info) {
        this.info = fixNewLine(info);
    }

    public void setError(String error) {
        this.error = fixNewLine(error);
    }

    public void setData(Object data) {
        if (data instanceof String) {
            this.data = StringEscapeUtils.escapeHtml4(StringEscapeUtils.unescapeJava(fixNewLine((String) data)));
        } else {
            this.data = data;
        }
    }

    private String fixNewLine(String text) {
        return text.replace("\r", "\\r").replace("\n", "\\n");

    }
    public String toJSON() {
        return gson.toJson(this);
    }

    public void setError(Exception exception) {
        final String exceptionStackTrace = ExceptionUtils.getExceptionStackTrace(exception);
        String[] splitExceptions = exceptionStackTrace.split("\n");
        splitExceptions = Arrays.copyOf(splitExceptions, 10);
        final String joinedExceptions = StringUtils.join(splitExceptions, "<br/>");
        setError(joinedExceptions);
    }

}
