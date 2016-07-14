package ru.sbt.bpm.mock.spring.context.generator;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */


public interface IContextGeneratable {

	generated.springframework.beans.ObjectFactory beanFactory = new generated.springframework.beans
			.ObjectFactory();

	generated.springframework.integration.ObjectFactory integrationFactory = new generated.springframework
			.integration.ObjectFactory();

	generated.springframework.integration.jms.ObjectFactory jmsFactory = new generated.springframework
			.integration.jms.ObjectFactory();

}
