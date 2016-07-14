package ru.sbt.bpm.mock.spring.service;

import org.apache.commons.io.FileUtils;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author sbt-bochev-as on 14.07.2016.
 *         <p/>
 *         Company: SBT - Moscow
 */
public class AbstractVirtualHttpServerTransactionalTestNGSpringContextTests extends AbstractTransactionalTestNGSpringContextTests {
    private Server server;

    @AfterClass(alwaysRun = true)
    @Override
    public void springTestContextAfterTestClass() throws Exception {
        server.stop();
    }

    @BeforeClass(alwaysRun = true)
    @Override
    public void springTestContextBeforeTestClass() throws Exception {
        super.springTestContextBeforeTestClass();
        server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(MockServlet.class, "/");
        server.start();

    }

    @BeforeClass(alwaysRun = true)
    @Override
    protected void springTestContextPrepareTestInstance() throws Exception {
        super.springTestContextPrepareTestInstance();
    }

    @SuppressWarnings("serial")
    public static class MockServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            URL basePath = this.getClass().getClassLoader().getResource("");
            assert basePath != null;
            String fileName = basePath.getPath() + "WEB-INF/xsd/CRM" + request.getServletPath().substring("path/to/dir/".length());
            File file = new File(fileName);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(FileUtils.readFileToString(file, "UTF-8"));
        }
    }
}
