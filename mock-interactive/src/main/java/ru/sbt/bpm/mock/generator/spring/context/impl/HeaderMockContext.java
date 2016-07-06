package ru.sbt.bpm.mock.generator.spring.context.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sbt.bpm.mock.generator.spring.context.IMockContext;

/**
 * Created by sbt-hodakovskiy-da on 22.06.2016.
 * Заголовок mockapp-servlet
 */
@Slf4j
@NoArgsConstructor
public class HeaderMockContext implements IMockContext {

	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	                                     "<beans xmlns:int=\"http://www.springframework.org/schema/integration\"\n"
	                                     + "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
	                                     + "     xmlns=\"http://www.springframework.org/schema/beans\"\n"
	                                     + "     xmlns:jms=\"http://www.springframework.org/schema/integration/jms\"\n"
	                                     + "     xsi:schemaLocation=\"http://www.springframework.org/schema/beans        http://www.springframework.org/schema/beans/spring-beans.xsd\n"
	                                     + "                          http://www.springframework.org/schema/integration    http://www.springframework.org/schema/integration/spring-integration.xsd\n"
	                                     + "        http://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration.xsd\n"
	                                     + "        http://www.springframework.org/schema/integration/jms http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd\">";

	private static final String IMPORTS = "<import resource=\"../contextConfigs/base-config.xml\"/>\n"
	                                    + "<import resource=\"../contextConfigs/logging-config.xml\"/>";

	@Override
	public String getContext () {
		return HEADER + IMPORTS;
	}
}
