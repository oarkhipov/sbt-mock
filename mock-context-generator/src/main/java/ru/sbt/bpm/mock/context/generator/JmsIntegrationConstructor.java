package ru.sbt.bpm.mock.context.generator;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

public class JmsIntegrationConstructor implements IContextGeneratable {

	public generated.springframework.integration.jms.ObjectFactory getIntegrationFactory() {
		return jmsFactory;
	}

}
