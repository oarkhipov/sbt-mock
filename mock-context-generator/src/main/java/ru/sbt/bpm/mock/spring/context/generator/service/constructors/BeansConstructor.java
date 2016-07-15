package ru.sbt.bpm.mock.spring.context.generator.service.constructors;

import generated.springframework.beans.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple2;
import ru.sbt.bpm.mock.spring.context.generator.IContextGeneratable;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
@Service
public class BeansConstructor implements IContextGeneratable {

	/**
	 * Создание beans
	 * <beans xmlns="http://www.springframework.org/schema/beans"></beans>
	 *
	 * @return beans
	 */
	public Beans createBeans () {
		return beanFactory.createBeans();
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class=""/>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @return
	 */
	public Beans createBean (Beans beans, String className) {
		return createBean(beans, className, null, new ArrayList<Tuple2<String, String>>(), new HashMap<String,
				String>(), null);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="" id=""/>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param id
	 * @return
	 */
	public Beans createBean (Beans beans, String className, String id) {
		return createBean(beans, className, id, new ArrayList<Tuple2<String, String>>(), new HashMap<String, String>
				(), null);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="" id=""/>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param id
	 * @param comment
	 * @return
	 */
	public Beans createBean (Beans beans, String className, String id, String comment) {
		return createBean(beans, className, id, new ArrayList<Tuple2<String, String>>(), new HashMap<String, String>
				(), comment);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="" id="">
	 * <constructor-arg value="" type=""/>
	 * </bean>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param id
	 * @param constructorArgValues
	 * @return
	 */
	public Beans createBean (generated.springframework.beans.Beans beans, String
			className, String id, List<Tuple2<String, String>> constructorArgValues) {
		return createBean(beans, className, id, constructorArgValues, new HashMap<String, String>(), null);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="" id="">
	 * <constructor-arg value="" type=""/>
	 * </bean>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param id
	 * @param constructorArgValues
	 * @param comment
	 * @return
	 */
	public Beans createBean (generated.springframework.beans.Beans beans, String
			className, String id, List<Tuple2<String, String>> constructorArgValues, String comment) {
		return createBean(beans, className, id, constructorArgValues, new HashMap<String, String>(), comment);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="">
	 * <constructor-arg value="" type=""/>
	 * </bean>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param constructorArgValues
	 * @return
	 */
	public Beans createBean (generated.springframework.beans.Beans beans, String className, List<Tuple2<String,
			String>> constructorArgValues) {
		return createBean(beans, className, null, constructorArgValues, new HashMap<String, String>(), null);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="">
	 * <constructor-arg value="" type=""/>
	 * </bean>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param constructorArgValues
	 * @param comment
	 * @return
	 */
	public Beans createBean (generated.springframework.beans.Beans beans, String className, List<Tuple2<String,
			String>> constructorArgValues, String comment) {
		return createBean(beans, className, null, constructorArgValues, new HashMap<String, String>(), comment);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="" id="">
	 * <property name="" value=""/>
	 * </bean>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param id
	 * @param properties
	 * @return
	 */
	public Beans createBean (Beans beans, String className, String id, Map<String, String> properties) {
		return createBean(beans, className, id, new ArrayList<Tuple2<String, String>>(), properties, null);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <bean class="" id="">
	 * <property name="" value=""/>
	 * </bean>
	 * </beans>
	 *
	 * @param beans
	 * @param className
	 * @param id
	 * @param properties
	 * @param comment
	 * @return
	 */
	public Beans createBean (Beans beans, String className, String id, Map<String, String> properties, String comment) {
		return createBean(beans, className, id, new ArrayList<Tuple2<String, String>>(), properties, comment);
	}

	/**
	 * Создание обычного bean для вложения в другие элементы
	 *
	 * @param className
	 * @param constructorArgs
	 * @return
	 */
	public generated.springframework.beans.Bean createBean (String className, List<Tuple2<String, String>>
			constructorArgs) {
		return createBean(className, "", createListConstructorArg(constructorArgs), new
				ArrayList<JAXBElement<PropertyType>>(), null);
	}

	/**
	 * Создание обычного bean для вложения в другие элементы
	 *
	 * @param className
	 * @param constructorArgs
	 * @param comment
	 * @return
	 */
	public generated.springframework.beans.Bean createBean (String className, List<Tuple2<String, String>>
			constructorArgs, String comment) {
		return createBean(className, "", createListConstructorArg(constructorArgs), new
				ArrayList<JAXBElement<PropertyType>>(), comment);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 * <import resource=""/>
	 * </beans>
	 *
	 * @param beans   -  <beans></beans> основной элемент хранения bean
	 * @param imports - список ресурсов импорта
	 * @return
	 */
	public Beans createImports (generated.springframework.beans.Beans beans, List<String> imports) {
		beans.getImportOrAliasOrBean().addAll(createListImports((String[]) imports.toArray()));
		return beans;
	}

	/**
	 * Создание списка импортов ресурсов
	 *
	 * @param imports - список ресурсов
	 * @return
	 */
	private List<Import> createListImports (String... imports) {
		List<Import> listImports = new ArrayList<Import>();
		for (String importName : imports)
			listImports.add(createImport(importName));
		return listImports;
	}

	/**
	 * <import resource=""/>
	 *
	 * @param importValue значение ресурса для импорта
	 * @return
	 */
	private Import createImport (String importValue) {
		generated.springframework.beans.Import resource = beanFactory.createImport();
		resource.setResource(importValue);
		return resource;
	}

	/**
	 * Создание списка аргументов конструктора
	 *
	 * @param values список значений аргументов конструктора
	 * @return
	 */
	private List<ConstructorArg> createListConstructorArg (List<Tuple2<String, String>> values) {
		List<ConstructorArg> args = new ArrayList<ConstructorArg>();
		for (Tuple2<String, String> value : values)
			args.add(createConstructorAgr(value.getT1(), value.getT2()));
		return args;
	}

	/**
	 * <constructor-arg value="" type=""/>
	 *
	 * @param value - значние аргумента конструктора
	 * @param type  - тип аргумента конструктора
	 * @return
	 */
	private ConstructorArg createConstructorAgr (String value, String type) {
		ConstructorArg arg = beanFactory.createConstructorArg();
		arg.setValue(value);
		if (type != null && !type.isEmpty())
			arg.setType(type);
		return arg;
	}

	/**
	 * Создание списка свойств
	 *
	 * @param properties Map свойств
	 * @return
	 */
	private List<JAXBElement<PropertyType>> createProperties (Map<String, String> properties) {
		List<JAXBElement<PropertyType>> listProperties = new ArrayList<JAXBElement<PropertyType>>();
		for (String propertyName : properties.keySet())
			listProperties.add(createProperty(createPropertyType(propertyName, properties.get(propertyName))));
		return listProperties;
	}

	/**
	 * <property name="" value=""/>
	 *
	 * @param propertyType - свойство
	 * @return
	 */
	private JAXBElement<PropertyType> createProperty (PropertyType propertyType) {
		return beanFactory.createProperty(propertyType);
	}

	/**
	 * Создание PropertyType
	 *
	 * @param propertyName  - имя свойства
	 * @param propertyValue - значение свойства
	 * @return
	 */
	private PropertyType createPropertyType (String propertyName, String
			propertyValue) {
		PropertyType propertyType = beanFactory.createPropertyType();
		propertyType.setName(propertyName);
		propertyType.setValue(propertyValue);
		return propertyType;
	}

	/**
	 * Создание
	 * <bean class="" id="">
	 * <constructor-arg value=""/>
	 * <property name="" value=""/>
	 * <constructor-arg value="" type=""/>
	 * </bean>
	 *
	 * @param beans                - <beans></beans> основной элемент хранения bean
	 * @param className            - имя класса
	 * @param id                   - id
	 * @param constructorArgValues агрументы конструктора
	 * @param properties           - свойства
	 * @param comment
	 * @return <beans></beans>
	 */
	private Beans createBean (@NonNull Beans beans, String className, String id,
	                          List<Tuple2<String, String>> constructorArgValues,
	                          Map<String, String> properties, String comment) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, createListConstructorArg(constructorArgValues),
		                                              createProperties(properties), comment));
		return beans;
	}

	/**
	 * Создание
	 * <bean class="" id="">
	 * <constructor-arg value=""/>
	 * <property name="" value=""/>
	 * <constructor-arg value="" type=""/>
	 * </bean>
	 *
	 * @param className  - имя класса
	 * @param id         - id
	 * @param args       - агрументы конструктора
	 * @param properties - свойства
	 * @param comment
	 * @return возвщается объект bean
	 */
	private Bean createBean (String className, String id, List<ConstructorArg> args, List<JAXBElement<PropertyType>>
			properties, String comment) {
		Bean bean = beanFactory.createBean();
		if (id != null && !id.isEmpty())
			bean.setId(id);
		if (className != null && !className.isEmpty())
			bean.setClazz(className);
		if (args != null && !args.isEmpty())
			bean.getMetaOrConstructorArgOrProperty().addAll(args);
		if (properties != null && !properties.isEmpty())
			bean.getMetaOrConstructorArgOrProperty().addAll(properties);
		if (comment != null && !comment.isEmpty())
			bean.setComment(comment);
		return bean;
	}

	public ObjectFactory getBeanFactory () {
		return beanFactory;
	}
}
