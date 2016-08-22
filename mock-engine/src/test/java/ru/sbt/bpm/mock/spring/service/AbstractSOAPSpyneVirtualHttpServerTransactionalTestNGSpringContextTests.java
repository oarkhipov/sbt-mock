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

package ru.sbt.bpm.mock.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.util.StreamUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author sbt-bochev-as on 15.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
@Slf4j
public class AbstractSOAPSpyneVirtualHttpServerTransactionalTestNGSpringContextTests extends AbstractTransactionalTestNGSpringContextTests {

    public static final String SIMPLE_REQUEST = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <spy:say_hello/>" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public static final String SIMPLE_RESPONSE = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spy=\"spyne.examples.hello\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <spy:say_helloResponse>\n" +
            "         <!--Optional:-->\n" +
            "         <spy:say_helloResult>\n" +
            "            <!--Zero or more repetitions:-->\n" +
            "            <spy:string>test</spy:string>\n" +
            "         </spy:say_helloResult>\n" +
            "      </spy:say_helloResponse>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    private Server server;

    @BeforeClass(alwaysRun = true)
    @Override
    protected void springTestContextPrepareTestInstance() throws Exception {
        super.springTestContextPrepareTestInstance();
    }

    @SuppressWarnings("Duplicates")
    @BeforeClass(alwaysRun = true)
    @Override
    protected void springTestContextBeforeTestClass() throws Exception {
        super.springTestContextBeforeTestClass();
        server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(MockServlet.class, "/");
        server.start();
    }

    @AfterClass(alwaysRun = true)
    @Override
    protected void springTestContextAfterTestClass() throws Exception {
        super.springTestContextAfterTestClass();
        if (server != null) {
            server.stop();
        }
    }

    @SuppressWarnings("serial")
    public static class MockServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            StringBuffer requestURL = request.getRequestURL();
            String headers = StringUtils.join(request.getHeaderNames(), ",");
            String body = StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset());
            log.info("got request \n" +
                    "URL: "+ requestURL.toString() + "\n" +
                    "Headers: " + headers + "\n" +
                    "Body: " + body);
            URL basePath = this.getClass().getClassLoader().getResource("");
            assert basePath != null;
            String fileName = basePath.getPath() + "wsdl" + request.getServletPath().substring("path/to/dir/".length());
            File file = new File(fileName);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(FileUtils.readFileToString(file, "UTF-8"));
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            doGet(request, response);
        }
    }
}
