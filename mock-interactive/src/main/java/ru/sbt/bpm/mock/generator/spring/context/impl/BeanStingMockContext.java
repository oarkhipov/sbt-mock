package ru.sbt.bpm.mock.generator.spring.context.impl;

import ru.sbt.bpm.mock.generator.spring.context.IMockContext;

/**
 * Created by sbt-hodakovskiy-da on 22.06.2016.
 */

public class BeanStingMockContext implements IMockContext {

	private static final String BEAN_ID              = "<bean id=\"";
	private static final String BEAN_CLASS           = "\" class=\"java.lang.String\">\n";
	private static final String BEAN_CONSTRUCTOR_ARG = "<constructor-arg value=\"";
	private static final String BEAN_END             = "\"/>\n</bean>";




	@Override
	public String getContext () {
		return null;
	}
}
