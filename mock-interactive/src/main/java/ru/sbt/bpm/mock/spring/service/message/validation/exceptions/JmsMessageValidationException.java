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

package ru.sbt.bpm.mock.spring.service.message.validation.exceptions;

/**
 * @author sbt-bochev-as on 26.04.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class JmsMessageValidationException extends MessageValidationException {

    private String xpath;
    private String expectedValue;
    private String actualValue;

    public JmsMessageValidationException(String xpath) {
        this.xpath = xpath;
    }

    public JmsMessageValidationException(String xpath, String expectedValue, String actualValue) {
        this.xpath = xpath;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }

    @Override
    public String getMessage() {
        if (expectedValue == null && actualValue == null) {
            return "JMS Validation Exception: message did not pass xpath [" + xpath + "] validation!";
        } else {
            return "JMS Validation Exception: message did not pass xpath [" + xpath + "] validation!\n\n" +
                    "Expected [" + expectedValue + "] but actual is [" + actualValue + "].";
        }
    }
}
