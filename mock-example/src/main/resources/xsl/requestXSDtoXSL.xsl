<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:out="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService" >

    <xsl:import  href="XSDtoXSL.xsl"/>
    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:namespace-alias stylesheet-prefix="out" result-prefix="xsl"/>

    <!-- файл с темплейтом для soap header'а -->
    <xsl:include href="headerTemplate.xsl"/>
    <xsl:include href="xsltFunctions.xsl"/>

    <!--параметры, в которых указывается откуда и какой элемент брать как само тело сообщения-->
    <xsl:param name="operationsXSD" select="''"/>
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>

    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/>
    <!--выкидываем ошибку, если нам не дали имя тэга элемента-->
    <xsl:variable name="throwError" select="if ($rootElementName!='') then true() else error(QName('http://sbrf.ru/mockService', 'err01'),'rootElementName not defined')"/>

    <!-- Имя операции в заголвке -->
    <xsl:param name="operationName" select="$rootElementName"/>

    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="rootTypeName" select="mock:removeNamespaceAlias(/xsd:schema//xsd:element[@name=$rootElementName]/@type)"/>

    <!--путь к верхней xsd с объявлением рут-элементов-->
    <xsl:param name="parrentXSDPath" select="'../../xsd/CRM/CRM.xsd'"/>
    <xsl:param name="rootXSD" select="document($parrentXSDPath)/xsd:schema"/>

    <xsl:param name="targetNS" select="$operationXsdSchema/@targetNamespace"/>
    <xsl:param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
    <!-- TODO выбрать этот параметр автоматом -->
    <xsl:param name="systemName" select="'CRM'"/>

    <!--Этим параметром можно выбрать алиас, под которым будет отображаться target namespace. Лучше не менять. Сделанно для того, чтобы не делать алиаса когда он пустой-->
    <xsl:param name="tNSAlias" select="if ($targetNS!='') then 'tns' else ''"/>
    <xsl:variable name="tns_" select="if ($tNSAlias!='') then concat($tNSAlias, ':') else ''"/>
    <xsl:variable name="dataNS" select="if ($targetNS!='') then mock:addDataToNamespaceUrl($targetNS, $rootElementName) else concat('http://sbrf.ru/mockService/',$rootElementName,'/Data/')"/>

    <!--В этой переменной идет выбор заголовка между разными системами. Сейчас выбор захорлкожен-->
    <!--!!! этот выбор захардкожен !!!-->
    <xsl:param name="headerType" select="if (contains($targetNS, 'bbmo') or $targetNS='') then 'KD4' else 'NCP'"/>

    <!--алиас неймспейса, который используется в исходной xsd-->
    <xsl:param name="localTargetNSAlias" select="local-name($operationXsdSchema/namespace::*[.=$targetNS][string-length(local-name(.))>0])"/>

    <!-- инклюды схем -->
    <xsl:variable name="includeFilesList" select="$operationXsdSchema/xsd:include/@schemaLocation"/>
    <xsl:variable name="includeFilesDocs" select="document($includeFilesList)/xsd:schema"/>

    <!-- импорт схем -->
    <xsl:variable name="importFilesList" select="$operationXsdSchema/xsd:import/@schemaLocation"/>
    <xsl:variable name="importFilesNs" select="$operationXsdSchema/xsd:import/@namespace"/>
    <xsl:variable name="importFilesNsAlias" select="$operationXsdSchema/namespace::*[.=$importFilesNs]/local-name()"/>
    <xsl:variable name="importFilesDocs" select="document($importFilesList)/xsd:schema"/>

    <xsl:variable name="xsdTagsToImport" select="tokenize('element complexType',' ')"/>
    <xsl:variable name="atributesWithTypes" select="tokenize('ref base type',' ')"/>

    <xsl:variable name="typesList" select="$operationXsdSchema/*[local-name()=$xsdTagsToImport] | $includeFilesDocs/*[local-name()=$xsdTagsToImport] | $importFilesDocs/*[local-name()=$xsdTagsToImport]"/>

    <xsl:variable name="type" select="'request'"/>

    <!--переменная для отображение отладочных сообщений-->
    <xsl:variable name="DEBUG" select="true()"/>

    <xsl:template match="xsd:schema">
        <xsl:element name="xsl:stylesheet">
            <xsl:if test="$targetNS!=''">
                <xsl:namespace name="{$tNSAlias}" select="$targetNS"/>
            </xsl:if>
            <xsl:namespace name="rsd" select="$dataNS"/>
            <xsl:if test="$parrentNS!=''">
                <xsl:namespace name="{$systemName}" select="$parrentNS"/>
            </xsl:if>
            <xsl:attribute name="version">1.0</xsl:attribute>
            <xsl:call-template name="headerDeclaration"/>
            <xsl:text>&#xA;</xsl:text>
            <xsl:call-template name="bodyDeclaration"/>
            <xsl:text>&#xA;&#xA;</xsl:text>


            <xsl:variable name="baseContainer" select="if (count($typesList[local-name()='complexType'][@name=$rootTypeName])>0) then ($typesList[local-name()='complexType'][@name=$rootTypeName])[1] else ($typesList[@name=$rootElementName])[1]"/>

            <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
            <!--<xsl:variable name="typesToTemplate" select="$baseContainer//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>-->
            <!--<xsl:comment>test <xsl:value-of select="$typesToTemplate"/></xsl:comment>-->
            <xsl:if test="$DEBUG">
                <xsl:comment>baseContainer if <xsl:value-of select="$rootTypeName"/> - <xsl:value-of select="count($typesList[local-name()='complexType'][@name=$rootTypeName])"/>  <xsl:value-of select="$rootElementName"/></xsl:comment>
                <xsl:comment>all types <xsl:value-of select="$typesList/@name"/></xsl:comment>
                <xsl:comment>types to import <xsl:value-of select="$typesList[@name=mock:typesToImport($baseContainer)]/@name"/></xsl:comment>
             </xsl:if>

            <xsl:apply-templates select="$typesList[@name=mock:typesToImport($baseContainer)]" mode="template"/>
            <xsl:apply-templates select="$baseContainer" mode="base"/>
            <!--<xsl:comment>test <xsl:value-of select="count(mock:typesToImport($baseContainer))"/></xsl:comment>-->
            <!--<xsl:comment>test <xsl:value-of select="$typesList/@name"/></xsl:comment>-->
            <!--<xsl:comment>test <xsl:value-of select="$rootTypeName"/></xsl:comment>-->
            <!--<xsl:comment>test <xsl:value-of select="/xsd:schema/xsd:element/@name"/></xsl:comment>-->
            <!--<xsl:comment>test <xsl:value-of select="$typesList[./@name=$typesToTemplate]/@name"/></xsl:comment>-->
        </xsl:element>
    </xsl:template>

    <!--Объявления заголовка - шапка, входные параметры-->
    <xsl:template name="headerDeclaration">
        <xsl:text>&#xA;</xsl:text>
        <xsl:element name="xsl:import">
            <xsl:attribute name="href"><xsl:call-template name="headerTemplatePath">
                <xsl:with-param name="headerType" select="$headerType"/>
            </xsl:call-template></xsl:attribute>
        </xsl:element>

        <xsl:comment>опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь</xsl:comment>
        <xsl:text>&#xA;</xsl:text>

        <xsl:element name="xsl:output">
            <xsl:attribute name="method">xml</xsl:attribute>
            <xsl:attribute name="indent">yes</xsl:attribute>
            <xsl:attribute name="omit-xml-declaration">yes</xsl:attribute>
        </xsl:element>


        <xsl:element name="xsl:param">
            <xsl:attribute name="name">name</xsl:attribute>
            <xsl:attribute name="select">all</xsl:attribute>
        </xsl:element>

        <xsl:call-template name="xslTeplateHeaderDeclaration">
            <xsl:with-param name="headerType" select="$headerType"/>
        </xsl:call-template>

        <xsl:text>&#xA;</xsl:text>

    </xsl:template>


    <xsl:template name="bodyDeclaration">
        <xsl:element name="xsl:template">
            <xsl:attribute name="match">/</xsl:attribute>
            <xsl:element name="xsl:variable">
                <xsl:attribute name="name">data</xsl:attribute>
                <xsl:attribute name="select">//rsd:data</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:variable">
                <xsl:attribute name="name">linkedTag</xsl:attribute>
                <xsl:attribute name="select">$name</xsl:attribute>
            </xsl:element>
            <xsl:call-template name="xslTeplateDeclaration">
                <xsl:with-param name="headerType" select="$headerType"/>
                <xsl:with-param name="operationName" select="$operationName"/>
                <xsl:with-param name="type" select="'request'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>