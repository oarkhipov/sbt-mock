<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:out="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService" >

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:namespace-alias stylesheet-prefix="out" result-prefix="xsl"/>

    <!-- файл с темплейтом для soap header'а -->
    <xsl:include href="headerTemplate.xsl"/>
    <xsl:include href="xsltFunctions.xsl"/>

    <!--параметры, в которых указывается откуда и какой элемент брать как само тело сообщения-->
    <xsl:param name="operationsXSD" select="''"/>
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>

    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/><!-- Todo ошибка если не задано -->

    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="rootTypeName" select="mock:removeNamespaceAlias(/xsd:schema//xsd:element[@name=$rootElementName]/@type)"/>

    <!--путь к верхней xsd с объявлением рут-элементов-->
    <xsl:param name="parrentXSDPath" select="'../../xsd/CRM/CRM.xsd'"/>
    <xsl:param name="rootXSD" select="document($parrentXSDPath)/xsd:schema"/>

    <xsl:param name="targetNS" select="$operationXsdSchema/@targetNamespace"/>
    <xsl:param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
    <!-- TODO выбрать этот параметр автоматом -->
    <xsl:param name="systemName" select="'CRM'"/>

    <!--В этой переменной идет выбор заголовка между разными системами. Сейчас выбор захорлкожен-->
    <!--!!! этот выбор захардкожен !!!-->
    <xsl:param name="headerType" select="if (/xsd:schema//xsd:element[@ref='kd4:KD4SoapHeaderV2']) then 'KD4' else 'NCP'"/>

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


    <xsl:template match="xsd:schema">
        <xsl:element name="xsl:stylesheet">
            <!--<xsl:for-each select="namespace::*">-->
                <!--<xsl:if test=". != 'http://www.w3.org/2001/XMLSchema'">-->
                    <!--<xsl:namespace name="{local-name(.)}" select="."/>-->
                <!--</xsl:if>-->
            <!--</xsl:for-each>-->
            <xsl:namespace name="tns" select="$targetNS"/>
            <xsl:namespace name="rsd" select="mock:addDataToNamespaceUrl($targetNS)"/>
            <!--<xsl:namespace name="soap" select="$soapEnvNS"/>-->
            <xsl:namespace name="{$systemName}" select="$parrentNS"/>
            <xsl:attribute name="version">1.0</xsl:attribute>
            <xsl:call-template name="headerDeclaration"/>
            <xsl:text>&#xA;</xsl:text>
            <xsl:call-template name="bodyDeclaration"/>
            <xsl:text>&#xA;&#xA;</xsl:text>

            <xsl:variable name="baseContainer" select="$typesList[@name=$rootTypeName or @name=$rootElementName]"/>

            <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
            <xsl:variable name="typesToTemplate" select="$baseContainer//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>
            <!--<xsl:comment>test <xsl:value-of select="$typesToTemplate"/></xsl:comment>-->


            <xsl:apply-templates select="$typesList[./@name=$typesToTemplate]" mode="template"/>
            <xsl:apply-templates select="$baseContainer" mode="base"/>
            <!--<xsl:comment>test <xsl:value-of select="$rootElementName"/></xsl:comment>-->
            <!--<xsl:comment>test <xsl:value-of select="$typesList/@name"/></xsl:comment>-->
            <!--<xsl:comment>test <xsl:value-of select="$typesList[./@name=$typesToTemplate]/@name"/></xsl:comment>-->
        </xsl:element>
    </xsl:template>

    <!--Объявления заголовка - шапка, входные параметры-->
    <xsl:template name="headerDeclaration">
        <xsl:text>&#xA;</xsl:text>
        <xsl:element name="xsl:import">
            <xsl:attribute name="href">../util/headerTemplate.xsl</xsl:attribute>
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

    </xsl:template>


    <xsl:template name="bodyDeclaration">
        <xsl:call-template name="xslTeplateDeclaration">
            <xsl:with-param name="headerType" select="$headerType"/>
            <xsl:with-param name="operationName" select="$rootElementName"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xsd:complexType" mode="template">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>
        <xsl:param name="type" select="self::*"/>
        <xsl:comment>test <xsl:value-of select="$typesList/@name"/></xsl:comment>
        <xsl:for-each select="$typesList//xsd:element[mock:removeNamespaceAlias(@type, $localTargetNSAlias) = $typeName]/@name">
            <xsl:element name="xsl:template">
                <xsl:attribute name="match">rsd:<xsl:value-of select="."/></xsl:attribute>
                <xsl:element name="tns:{.}"  namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:apply-templates select="$type//xsd:element" mode="templateInside"/>
                </xsl:element>
            </xsl:element>
            <xsl:text>&#xA;&#xA;</xsl:text>
        </xsl:for-each>

        <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
        <xsl:variable name="typesToTemplate" select=".//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>
        <!--<xsl:comment>test <xsl:value-of select="$typesToTemplate"/></xsl:comment>-->

        <xsl:apply-templates select="$typesList[./@name=$typesToTemplate]" mode="template"/>
    </xsl:template>

    <xsl:template match="xsd:element" mode="template">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>
        <xsl:element name="xsl:template">
            <xsl:attribute name="match">rsd:<xsl:value-of select="$typeName"/></xsl:attribute>
            <xsl:element name="tns:{$typeName}"  namespace="{$targetNS}">
                <xsl:namespace name="tns" select="$targetNS"/>
                <xsl:apply-templates select=".//xsd:element" mode="templateInside"/>
            </xsl:element>
        </xsl:element>
        <xsl:text>&#xA;&#xA;</xsl:text>

        <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
        <xsl:variable name="typesToTemplate" select=".//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>
        <!--<xsl:comment>test <xsl:value-of select="$typesToTemplate"/></xsl:comment>-->

        <xsl:apply-templates select="$typesList[./@name=$typesToTemplate]" mode="template"/>
    </xsl:template>

    <xsl:template match="*[local-name()=$xsdTagsToImport]" mode="base">
        <xsl:element name="xsl:template">
            <xsl:attribute name="name"><xsl:value-of select="$rootElementName"/></xsl:attribute>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">response</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">data</xsl:attribute>
            </xsl:element>
                <xsl:element name="xsl:element">
                    <xsl:attribute name="name"><xsl:value-of select="$systemName"/>:<xsl:value-of select="$rootElementName"/></xsl:attribute>
                    <xsl:apply-templates select=".//xsd:element" mode="InsideBase"/>
                </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@name]" mode="InsideBase" priority="1">
        <xsl:choose>
            <!--TODO рассмоьтреть случай, когда есть @maxOccurs. тогда надо длеать еще темплейт -->
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                        <xsl:namespace name="tns" select="$targetNS"/>
                        <xsl:element name="xsl:value-of">
                            <xsl:attribute name="select">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:element name="xsl:value-of" >
                        <xsl:attribute name="select">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    </xsl:element>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]" mode="InsideBase"  priority="2">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@type, $localTargetNSAlias)"/>
        <!--<xsl:comment> <xsl:value-of select="$typeName"/> </xsl:comment>-->
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@ref]" mode="InsideBase"  priority="3">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@ref, $localTargetNSAlias)"/>
        <!--<xsl:comment> <xsl:value-of select="$typeName"/> </xsl:comment>-->
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="$typeName"/></xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@name]" mode="templateInside"  priority="1">
        <xsl:choose>
            <!--TODO рассмоьтреть случай, когда есть @maxOccurs. тогда надо длеать еще темплейт -->
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                        <xsl:namespace name="tns" select="$targetNS"/>
                        <xsl:element name="xsl:value-of">
                            <xsl:attribute name="select">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:element name="xsl:value-of" >
                        <xsl:attribute name="select">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    </xsl:element>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]" mode="templateInside"  priority="2">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@type, $localTargetNSAlias)"/>
        <!--<xsl:comment> <xsl:value-of select="$typeName"/> </xsl:comment>-->
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@ref]" mode="templateInside"  priority="3">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@ref, $localTargetNSAlias)"/>
        <!--<xsl:comment> <xsl:value-of select="$typeName"/> </xsl:comment>-->
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
        </xsl:element>
    </xsl:template>


</xsl:stylesheet>