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

    <!--TODO пренести функции в xsltFunctions.xsl-->
    <xsl:function name="mock:typesToImport">
        <xsl:param name="baseElement"/>
        <xsl:variable name="importOnThislevel" select="mock:typesNeedingImport($baseElement)"/>
        <xsl:variable name="nextLevelElements" select="$baseElement | $typesList[@name=$importOnThislevel]"/>
        <xsl:choose>
            <xsl:when test="count($baseElement) &lt; count($nextLevelElements)">
                <xsl:variable name="allInside" select="$nextLevelElements | $typesList[@name=mock:typesToImport($nextLevelElements)]"/>
                <xsl:for-each select="$allInside/@name"><xsl:value-of select="."/></xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="$nextLevelElements/@name"><xsl:value-of select="."/></xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="mock:typesNeedingImport">
        <xsl:param name="baseElement"/>
        <xsl:variable name="extensionElementName" select="mock:removeNamespaceAlias($baseElement//xsd:extension/@base)"/>
        <xsl:variable name="nsAlias" select="mock:getNamespaceAlias($baseElement//xsd:extension/@base)"/>
        <xsl:variable name="ns" select="$baseElement/namespace::*[local-name()=$nsAlias]"/>
        <xsl:variable name="extensionElement" select="$typesList[@name=$extensionElementName and ./../@targetNamespace=$ns]"/>
        <xsl:variable name="importOnThislevel" select="($baseElement | $extensionElement)//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>
        <xsl:for-each select="$importOnThislevel"><xsl:value-of select="."/></xsl:for-each>
    </xsl:function>

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

            <xsl:variable name="baseContainer" select="if (count($typesList[@name=$rootTypeName])=1) then $typesList[@name=$rootTypeName] else $typesList[@name=$rootElementName]"/>

            <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
            <xsl:variable name="typesToTemplate" select="$baseContainer//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>
            <!--<xsl:comment>test <xsl:value-of select="$typesToTemplate"/></xsl:comment>-->


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

    <!--<xsl:template match="xsd:complexType" mode="template">-->
        <!--<xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>-->
        <!--<xsl:param name="type" select="self::*"/>-->

        <!--<xsl:variable name="ns" select="../@targetNamespace"/>-->
        <!--<xsl:variable name="nsAliasFromFile" select="$operationXsdSchema/namespace::*[.=$ns][string-length()>0][1]/local-name()"/>-->
        <!--<xsl:variable name="nsAlias" select="if ($ns = $targetNS) then 'tns' else-->
                                        <!--if (string-length($nsAliasFromFile)>0) then $nsAliasFromFile else 'ns1'"/>-->

        <!--&lt;!&ndash;<xsl:comment>test <xsl:value-of select="$typesList/@name"/></xsl:comment>&ndash;&gt;-->
        <!--<xsl:for-each select="$typesList//xsd:element[mock:removeNamespaceAlias(@type, $localTargetNSAlias) = $typeName]/@name">-->
            <!--<xsl:element name="xsl:template">-->
                <!--<xsl:attribute name="match">rsd:<xsl:value-of select="."/></xsl:attribute>-->
                <!--<xsl:element name="{$nsAlias}:{.}"  namespace="{$targetNS}">-->
                    <!--<xsl:if test="$ns!=''">-->
                        <!--<xsl:namespace name="{$nsAlias}" select="$ns"/>-->
                    <!--</xsl:if>-->
                    <!--<xsl:apply-templates select="$type//xsd:element" mode="Inside">-->
                        <!--<xsl:with-param name="dataPath" select="'./rsd:'"/>-->
                        <!--<xsl:with-param name="ns" select="$ns"/>-->
                        <!--<xsl:with-param name="nsAlias" select="$nsAlias"/>-->
                    <!--</xsl:apply-templates>-->
                <!--</xsl:element>-->
            <!--</xsl:element>-->
            <!--<xsl:text>&#xA;&#xA;</xsl:text>-->
        <!--</xsl:for-each>-->

    <!--</xsl:template>-->

    <!--<xsl:template match="xsd:element" mode="template">-->
        <!--<xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>-->

        <!--<xsl:variable name="ns" select="../@targetNamespace"/>-->
        <!--<xsl:variable name="nsAliasFromFile" select="$operationXsdSchema/namespace::*[.=$ns][string-length()>0][1]/local-name()"/>-->
        <!--<xsl:variable name="nsAlias" select="if ($ns = $targetNS) then 'tns' else-->
                                        <!--if (string-length($nsAliasFromFile)>0) then $nsAliasFromFile else 'ns1'"/>-->

        <!--<xsl:element name="xsl:template">-->
            <!--<xsl:attribute name="match">rsd:<xsl:value-of select="$typeName"/></xsl:attribute>-->
            <!--<xsl:element name="{$nsAlias}:{$typeName}"  namespace="{$ns}">-->
                <!--<xsl:if test="$nsAlias!=''">-->
                    <!--<xsl:namespace name="{$nsAlias}" select="$ns"/>-->
                <!--</xsl:if>-->
                <!--<xsl:apply-templates select="./xsd:complexType/xsd:sequence/xsd:element" mode="Inside">-->
                    <!--<xsl:with-param name="dataPath" select="'./rsd:'"/>-->
                    <!--<xsl:with-param name="ns" select="$ns"/>-->
                    <!--<xsl:with-param name="nsAlias" select="$nsAlias"/>-->
                <!--</xsl:apply-templates>-->
            <!--</xsl:element>-->
        <!--</xsl:element>-->
        <!--<xsl:text>&#xA;&#xA;</xsl:text>-->

    <!--</xsl:template>-->

    <xsl:template match="*[local-name()=$xsdTagsToImport]" mode="base">
        <xsl:variable name="mainElementNSAlias" select="if ($parrentNS!='') then (
                                                                                        if ($targetNS=$parrentNS) then $tns_ else concat($systemName,':')
                                                                                    ) else ''"/>

        <xsl:variable name="extensionElementName" select="mock:removeNamespaceAlias(.//xsd:extension/@base)"/>
        <xsl:variable name="usedNsAlias" select="mock:getNamespaceAlias(.//xsd:extension/@base)"/>
        <xsl:variable name="usedNs" select="./namespace::*[local-name()=$usedNsAlias]"/>
        <xsl:variable name="extensionElement" select="$typesList[@name=$extensionElementName and ./../@targetNamespace=$usedNs]"/>
        <!--<xsl:comment>test <xsl:value-of select="local-name()"/> name={<xsl:value-of select="@name"/>}</xsl:comment>-->
        <xsl:element name="xsl:template">
            <xsl:attribute name="name"><xsl:value-of select="$operationName"/></xsl:attribute>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">request</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">data</xsl:attribute>
            </xsl:element>
                <xsl:element name="xsl:element">
                    <xsl:attribute name="name"><xsl:value-of select="$mainElementNSAlias"/><xsl:value-of select="$rootElementName"/></xsl:attribute>
                    <xsl:apply-templates select="$extensionElement//xsd:element" mode="Inside">
                        <xsl:with-param name="dataPath" select="'$data/rsd:request[@name=$request]/rsd:'"/>
                        <xsl:with-param name="ns" select="$usedNs"/>
                        <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./xsd:sequence
                                                | ./xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
                                                | ./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element))" mode="Inside">
                        <xsl:with-param name="dataPath" select="'$data/rsd:request[@name=$request]/rsd:'"/>
                    </xsl:apply-templates>
                </xsl:element>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>