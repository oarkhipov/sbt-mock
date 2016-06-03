package ru.sbt.bpm.mock.product.ready;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Created by sbt-hodakovskiy-da on 03.06.2016.
 */

@Slf4j
@ContextConfiguration({"/env/mockapp-servlet-test.xml"})
@WebAppConfiguration("mock-interactive/src/main/webapp")
public class DataFileTest extends AbstractTestNGSpringContextTests {


	@Test
	public void tesDataFiles() {
		boolean assertSuccess = true;



		assertTrue(assertSuccess);
	}
}
