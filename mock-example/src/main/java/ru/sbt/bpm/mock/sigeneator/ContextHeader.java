package ru.sbt.bpm.mock.sigeneator;

/**
 * Created by sbt-hodakovskiy-da on 04.02.2015.
 * <p/>
 * Company: SBT - Saint-Petersburg
 */

// Генерация заголовка файла контекста
public class ContextHeader {

    // Header namespaces
    private static final Pair<String, String> BEANS_NAMESPACE = new Pair<String, String>("http://www.springframework.org/schema/beans", "http://www.springframework.org/schema/beans/spring-beans.xsd");
    private static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
    private static final  Pair<String, String> INT_NAMESPACE = new Pair<String, String>("http://www.springframework.org/schema/integration", "http://www.springframework.org/schema/integration/spring-integration.xsd");
    private static final Pair<String, String> JMS_NAMESPACE = new Pair<String, String>("http://www.springframework.org/schema/integration/jms", "http://www.springframework.org/schema/integration/jms/spring-integration-jms.xsd");
    private static final Pair<String, String> IX_NAMESPACE = new Pair<String, String>("http://www.springframework.org/schema/integration/xml", "http://www.springframework.org/schema/integration/xml/spring-integration-xml.xsd");
    private static final Pair<String, String> UTIL_NAMESPACE = new Pair<String, String>("http://www.springframework.org/schema/util", "http://www.springframework.org/schema/util/spring-util.xsd");
    private static final Pair<String, String> CONTEXT_NAMESPACE = new Pair<String, String>("http://www.springframework.org/schema/context", "http://www.springframework.org/schema/context/spring-context.xsd");
    private static final Pair<String, String> MVC_NAMESPACE = new Pair<String, String>("http://www.springframework.org/schema/mvc", "http://www.springframework.org/schema/mvc/spring-mvc.xsd");

    private static final String XMLNS = "xmlns";

    // Namespaces aliases
    private static final String BEANS_ALIAS = "beans";
    private static final String XSI_ALIAS = "xsi";
    private static final String INT_ALIAS = "int";
    private static final String JMS_ALIAS = "jms";
    private static final String IX_ALIAS = "ix";
    private static final String UTIL_ALIAS = "util";
    private static final String CONTEXT_ALIAS = "context";
    private static final String MVC_ALIAS = "mvc";

    // Флаги для переменных
//    private boolean beans;
    private boolean integration;
    private boolean jms;
    private boolean ix;
    private boolean util;
    private boolean context;
    private boolean mvc;

    // Добавляет все элементы в заголок по умолчанию
    public ContextHeader() {
//        this.beans = true;
        this.integration = true;
        this.jms = true;
        this.ix = true;
        this.util = true;
        this.context = true;
        this.mvc = true;
    }

    public ContextHeader(boolean integration, boolean jms, boolean ix, boolean util, boolean context, boolean mvc) {
//        this.beans = beans;
        this.integration = integration;
        this.jms = jms;
        this.ix = ix;
        this.util = util;
        this.context = context;
        this.mvc = mvc;
    }

    /**
     * Генерация заголовка spring context
     */
    public void generateContextHeader() {
        StringBuilder sb = new StringBuilder("<" + BEANS_ALIAS + ":" + BEANS_ALIAS);
        sb.append(XMLNS + ":" + XSI_ALIAS + "=\"" + XSI_NAMESPACE + "\"\n");

        StringBuilder sbSchemaLocation = new StringBuilder(XSI_ALIAS + ":" + "schemaLocation=\"");

        // Компонент beans постаянный, так как его alias ведет к основным её элементам
        sb.append(generateAliasesForNamespaces(BEANS_ALIAS, BEANS_NAMESPACE));
        sbSchemaLocation.append(generateSchemaLocationParams(BEANS_NAMESPACE));

//        generateTagContext(sb, sbSchemaLocation, beans, BEANS_ALIAS, BEANS_NAMESPACE);
        generateTagContext(sb, sbSchemaLocation, integration, INT_ALIAS, INT_NAMESPACE);
        generateTagContext(sb, sbSchemaLocation, jms, JMS_ALIAS, JMS_NAMESPACE);
        generateTagContext(sb, sbSchemaLocation, ix, IX_ALIAS, IX_NAMESPACE);
        generateTagContext(sb, sbSchemaLocation, util, UTIL_ALIAS, UTIL_NAMESPACE);
        generateTagContext(sb, sbSchemaLocation, context, CONTEXT_ALIAS, CONTEXT_NAMESPACE);
        generateTagContext(sb, sbSchemaLocation, mvc, MVC_ALIAS, MVC_NAMESPACE);

        sbSchemaLocation.append("\"\n");
        sb.append(sbSchemaLocation);
        sb.append(">\n\n");
    }

    public String generateEndTag() {
        return "</" + BEANS_ALIAS + ":" + BEANS_ALIAS + ">\n";
    }

    private void generateTagContext(StringBuilder sb, StringBuilder sbSchemaLocation, boolean param, String alias, Pair<String, String> component) {
        if (param) {
            sb.append(generateAliasesForNamespaces(alias, component));
            sbSchemaLocation.append(generateSchemaLocationParams(component));
        }
    }

    /**
     * Генерация алиасов для namespaces
     * @return
     */
    private String generateAliasesForNamespaces(String alias, Pair<String, String> component) {
        return XMLNS + ":" + alias + "=\"" + component.getaFirst() + "\"\n";
    }

    /**
     *
     * @param component
     * @return
     */
    private String generateSchemaLocationParams(Pair<String, String> component) {
        return component.getaFirst() + "\t" + component.getaSecond();
    }

    /**
     * Генерация аттрибута
     * @return
     */
    private String generateTagAttribute(String ns, String alias) {
        return ns + ":" + alias + "\n";
    }

//    public boolean isBeans() {
//        return beans;
//    }
//
//    public void setBeans(boolean beans) {
//        this.beans = beans;
//    }

    public boolean isIntegration() {
        return integration;
    }

    public void setIntegration(boolean integration) {
        this.integration = integration;
    }

    public boolean isJms() {
        return jms;
    }

    public void setJms(boolean jms) {
        this.jms = jms;
    }

    public boolean isIx() {
        return ix;
    }

    public void setIx(boolean ix) {
        this.ix = ix;
    }

    public boolean isUtil() {
        return util;
    }

    public void setUtil(boolean util) {
        this.util = util;
    }

    public boolean isContext() {
        return context;
    }

    public void setContext(boolean context) {
        this.context = context;
    }

    public boolean isMvc() {
        return mvc;
    }

    public void setMvc(boolean mvc) {
        this.mvc = mvc;
    }

    public static String getBeansAlias() {
        return BEANS_ALIAS;
    }

    public static String getXsiAlias() {
        return XSI_ALIAS;
    }

    public static String getIntAlias() {
        return INT_ALIAS;
    }

    public static String getJmsAlias() {
        return JMS_ALIAS;
    }

    public static String getIxAlias() {
        return IX_ALIAS;
    }

    public static String getUtilAlias() {
        return UTIL_ALIAS;
    }

    public static String getContextAlias() {
        return CONTEXT_ALIAS;
    }

    public static String getMvcAlias() {
        return MVC_ALIAS;
    }
}