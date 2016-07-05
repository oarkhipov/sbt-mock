package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.ConstructorArg;
import generated.springframework.beans.Import;
import generated.springframework.beans.PropertyType;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
// TODO зарефакторить всё на один большой метод, который будет всегда возвращать beans не нужные параметры передаются либо null, либо пустота
@Service
public class BeansConstructor implements IContextGeneratable  {

	/**
	 * Создание beans
	 * @return beans
	 */
	public generated.springframework.beans.Beans createBeans() {
		return beanFactory.createBeans();
	}

	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className) {
		beans.getImportOrAliasOrBean().add(createBean(className, null, new ArrayList<ConstructorArg>(), new ArrayList
				<PropertyType>()));
		return beans;
	}

	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className, String id) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, new ArrayList<ConstructorArg>(), new ArrayList
				<PropertyType>()));
		return beans;
	}

	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className, String id, List<Tuple2<String, String>> constructorArgValues) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, createListConstructorArg(constructorArgValues), new ArrayList<PropertyType>()));
		return beans;
	}

	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className,String id, Map<String, String> properties) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, new ArrayList<ConstructorArg>(), createProperties(properties)));
		return beans;
	}

	public generated.springframework.beans.Beans createImports(generated.springframework.beans.Beans beans, List<String> imports) {
		beans.getImportOrAliasOrBean().addAll(createListImports((String[]) imports.toArray()));
		return beans;
	}

	private List<generated.springframework.beans.Import> createListImports(String... imports) {
		List<generated.springframework.beans.Import> listImports = new ArrayList<Import>();
		for (String importName : imports)
			listImports.add(createImport(importName));
		return listImports;
	}

	private generated.springframework.beans.Import createImport (String importValue) {
		generated.springframework.beans.Import resource = beanFactory.createImport();
		resource.setResource(importValue);
		return resource;
	}

	private List<generated.springframework.beans.ConstructorArg> createListConstructorArg (List<Tuple2<String, String>> values) {
		List<generated.springframework.beans.ConstructorArg> args = new ArrayList<ConstructorArg>();
		for (Tuple2<String, String> value : values)
			args.add(createConstructorAgr(value.getT1(), value.getT2()));
		return args;
	}

	private generated.springframework.beans.ConstructorArg createConstructorAgr(String value, String type) {
		generated.springframework.beans.ConstructorArg arg = beanFactory.createConstructorArg();
		arg.setValue(value);
		if (type != null && !type.isEmpty())
			arg.setType(type);
		return arg;
	}

	private List<generated.springframework.beans.PropertyType> createProperties(Map<String, String> properties) {
		List<generated.springframework.beans.PropertyType> listProperties = new ArrayList<PropertyType>();
		for (String propertyName : properties.keySet())
			listProperties.add(createProperty(propertyName, properties.get(propertyName)));
		return listProperties;
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
	 * @param args
	 * @param properties
	 * @return
	 */
	private generated.springframework.beans.Bean createBean(String className, String id, List<generated.springframework.beans.ConstructorArg> args, List<generated.springframework.beans.PropertyType> properties) {
		generated.springframework.beans.Bean bean = beanFactory.createBean();
		if (id != null && !id.isEmpty())
			bean.setId(id);
		if (className != null && !className.isEmpty())
			bean.setClazz(className);
		if (args != null && !args.isEmpty())
			bean.getMetaOrConstructorArgOrProperty().addAll(args);
		if (properties != null && !properties.isEmpty())
			bean.getMetaOrConstructorArgOrProperty().addAll(properties);
		return bean;
	}
}
