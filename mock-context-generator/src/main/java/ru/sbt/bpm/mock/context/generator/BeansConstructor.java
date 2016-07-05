package ru.sbt.bpm.mock.context.generator;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */

@Slf4j
public class BeansConstructor implements IContextGeneratable  {

	/**
	 * Создание beans
	 * @return
	 */
	public generated.springframework.beans.Beans createBeans() {
		return beanFactory.createBeans();
	}

	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className) {
		beans.getImportOrAliasOrBean().add(createBean(className, null, null, null));
		return beans;
	}

	public generated.springframework.beans.Beans cteateBean(generated.springframework.beans.Beans beans, String className, String id) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, null, null));
		return beans;
	}

	public generated.springframework.beans.Beans cteateBean(generated.springframework.beans.Beans beans, String className, String id, String constructorArgValue) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, createConstructorArg(constructorArgValue), null));
		return beans;
	}

	public generated.springframework.beans.Beans cteateBean(generated.springframework.beans.Beans beans, String className, String id, String propertyName, String propertyValue) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, null, createProperty(propertyName, propertyValue)));
		return beans;
	}

	private generated.springframework.beans.ConstructorArg createConstructorArg(String value) {
		generated.springframework.beans.ConstructorArg arg = beanFactory.createConstructorArg();
		arg.setValue(value);
		return arg;
	}

	private generated.springframework.beans.PropertyType createProperty(String propertyName, String propertyValue) {
		generated.springframework.beans.PropertyType property = beanFactory.createPropertyType();
		property.setName(propertyName);
		property.setValue(propertyValue);
		return property;
	}

	/**
	 * Создание bean
	 * @param className
	 * @param id
	 * @param arg
	 * @param property
	 * @return
	 */
	private generated.springframework.beans.Bean createBean(String className, String id, generated.springframework.beans.ConstructorArg arg, generated.springframework.beans.PropertyType property) {
		generated.springframework.beans.Bean bean = beanFactory.createBean();
		if (className != null)
			bean.setClazz(className);
		if (id != null)
			bean.setId(id);
		if (arg != null)
			bean.getMetaOrConstructorArgOrProperty().add(arg);
		if (property != null)
			bean.getMetaOrConstructorArgOrProperty().add(property);
		return bean;
	}
}
