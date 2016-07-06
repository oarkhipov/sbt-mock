package ru.sbt.bpm.mock.context.generator;

import generated.springframework.beans.Beans;
import generated.springframework.beans.ConstructorArg;
import generated.springframework.beans.Import;
import generated.springframework.beans.PropertyType;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import reactor.tuple.Tuple2;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbt-hodakovskiy-da on 05.07.2016.
 */
@Service
public class BeansConstructor implements IContextGeneratable  {

	/**
	 * Создание beans
	 * <beans xmlns="http://www.springframework.org/schema/beans"></beans>
	 * @return beans
	 */
	public generated.springframework.beans.Beans createBeans() {
		return beanFactory.createBeans();
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 *      <bean class=""/>
	 * </beans>
	 * @param beans
	 * @param className
	 * @return
	 */
	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className) {
		return createBean(beans, className, null, new ArrayList<Tuple2<String, String>>(), new HashMap<String,
				String>());
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 *      <bean class="" id=""/>
	 * </beans>
	 * @param beans
	 * @param className
	 * @param id
	 * @return
	 */
	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className, String id) {
		return createBean(beans, className, id, new ArrayList<Tuple2<String, String>>(), new HashMap<String, String>());
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 *      <bean class="" id="">
	 *          <constructor-arg value="" type=""/>
	 *      </bean>
	 * </beans>
	 * @param beans
	 * @param className
	 * @param id
	 * @param constructorArgValues
	 * @return
	 */
	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className, String id, List<Tuple2<String, String>> constructorArgValues) {
		return createBean(beans, className, id, constructorArgValues, new HashMap<String, String>());
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 *      <bean class="">
	 *          <constructor-arg value="" type=""/>
	 *      </bean>
	 * </beans>
	 * @param beans
	 * @param className
	 * @param constructorArgValues
	 * @return
	 */
	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className, List<Tuple2<String, String>> constructorArgValues) {
		return createBean(beans, className, null, constructorArgValues, new HashMap<String, String>());
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 *      <bean class="" id="">
	 *          <property name="" value=""/>
	 *      </bean>
	 * </beans>
	 * @param beans
	 * @param className
	 * @param id
	 * @param properties
	 * @return
	 */
	public generated.springframework.beans.Beans createBean(generated.springframework.beans.Beans beans, String className,String id, Map<String, String> properties) {
		return createBean(beans, className, id, new ArrayList<Tuple2<String, String>>(), properties);
	}

	/**
	 * <beans xmlns="http://www.springframework.org/schema/beans">
	 *      <import resource=""/>
	 * </beans>
	 * @param beans -  <beans></beans> основной элемент хранения bean
	 * @param imports - список ресурсов импорта
	 * @return
	 */
	public generated.springframework.beans.Beans createImports(generated.springframework.beans.Beans beans,
	                                                           List<String> imports) {
		beans.getImportOrAliasOrBean().addAll(createListImports((String[]) imports.toArray()));
		return beans;
	}

	/**
	 * Создание списка импортов ресурсов
	 * @param imports - список ресурсов
	 * @return
	 */
	private List<generated.springframework.beans.Import> createListImports(String... imports) {
		List<generated.springframework.beans.Import> listImports = new ArrayList<Import>();
		for (String importName : imports)
			listImports.add(createImport(importName));
		return listImports;
	}

	/**
	 * <import resource=""/>
	 * @param importValue значение ресурса для импорта
	 * @return
	 */
	private generated.springframework.beans.Import createImport (String importValue) {
		generated.springframework.beans.Import resource = beanFactory.createImport();
		resource.setResource(importValue);
		return resource;
	}

	/**
	 * Создание списка аргументов конструктора
	 * @param values список значений аргументов конструктора
	 * @return
	 */
	private List<generated.springframework.beans.ConstructorArg> createListConstructorArg (List<Tuple2<String, String>> values) {
		List<generated.springframework.beans.ConstructorArg> args = new ArrayList<ConstructorArg>();
		for (Tuple2<String, String> value : values)
			args.add(createConstructorAgr(value.getT1(), value.getT2()));
		return args;
	}

	/**
	 * <constructor-arg value="" type=""/>
	 * @param value - значние аргумента конструктора
	 * @param type - тип аргумента конструктора
	 * @return
	 */
	private generated.springframework.beans.ConstructorArg createConstructorAgr(String value, String type) {
		generated.springframework.beans.ConstructorArg arg = beanFactory.createConstructorArg();
		arg.setValue(value);
		if (type != null && !type.isEmpty())
			arg.setType(type);
		return arg;
	}

	/**
	 * Создание списка свойств
	 * @param properties Map свойств
	 * @return
	 */
	private List<JAXBElement<PropertyType>> createProperties(Map<String, String> properties) {
		List<JAXBElement<PropertyType>> listProperties = new ArrayList<JAXBElement<PropertyType>>();
		for (String propertyName : properties.keySet())
			listProperties.add(createProperty(createPropertyType(propertyName, properties.get(propertyName))));
		return listProperties;
	}

	/**
	 * <property name="" value=""/>
	 * @param propertyType - свойство
	 * @return
	 */
	private JAXBElement<PropertyType> createProperty(generated.springframework.beans.PropertyType propertyType) {
		return beanFactory.createProperty(propertyType);
	}

	/**
	 * Создание PropertyType
	 * @param propertyName - имя свойства
	 * @param propertyValue - значение свойства
	 * @return
	 */
	private generated.springframework.beans.PropertyType createPropertyType(String propertyName, String propertyValue) {
		generated.springframework.beans.PropertyType propertyType = beanFactory.createPropertyType();
		propertyType.setName(propertyName);
		propertyType.setValue(propertyValue);
		return propertyType;
	}

	/**
	 * Создание
	 * <bean class="" id="">
	 *     <constructor-arg value=""/>
	 *     <property name="" value=""/>
	 *     <constructor-arg value="" type=""/>
	 * </bean>
	 * @param beans - <beans></beans> основной элемент хранения bean
	 * @param className - имя класса
	 * @param id - id
	 * @param constructorArgValues агрументы конструктора
	 * @param properties - свойства
	 * @return <beans></beans>
	 */
	private generated.springframework.beans.Beans createBean(@NonNull Beans beans, String className, String id, List<Tuple2<String, String>> constructorArgValues, Map<String, String> properties) {
		beans.getImportOrAliasOrBean().add(createBean(className, id, createListConstructorArg(constructorArgValues), createProperties(properties)));
		return beans;
	}

	/**
	 * Создание
	 * <bean class="" id="">
	 *     <constructor-arg value=""/>
	 *     <property name="" value=""/>
	 *     <constructor-arg value="" type=""/>
	 * </bean>
	 * @param className - имя класса
	 * @param id - id
	 * @param args - агрументы конструктора
	 * @param properties - свойства
	 * @return возвщается объект bean
	 */
	private generated.springframework.beans.Bean createBean(String className, String id, List<generated.springframework.beans.ConstructorArg> args, List<JAXBElement<PropertyType>> properties) {
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

	public generated.springframework.beans.ObjectFactory getBeanFactory() {
		return beanFactory;
	}
}
