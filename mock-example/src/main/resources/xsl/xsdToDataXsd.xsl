<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService">
    <!-- xslt чтобы превратить xsd веб сервиса в data xsd - нашу xsd, которой должно соответствовать xml с данными.
     Представляет собой копипаст объявления с заменой неймспейсов-->
    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!--параметры, в которых указывается откуда и какой элемент брать как само тело сообщения-->
    <xsl:param name="operationsXSD" select="''"/>
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>

    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/>
    <!--выкидываем ошибку, если нам не дали имя тэга элемента-->
    <xsl:variable name="throwError" select="if ($rootElementName!='') then true() else error(QName('http://sbrf.ru/mockService', 'err01'),'rootElementName not defined')"/>

    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="rootTypeName" select="mock:removeNamespaceAlias(/xsd:schema//xsd:element[@name=$rootElementName]/@type)"/>

    <!--Как будет называться в итоге элемент-->
    <xsl:param name="newDataNodeTypeName" select="if (string-length($rootTypeName)>0) then $rootTypeName else $rootElementName"/>

    <!-- TODO выбрать этот параметр автоматом -->
    <xsl:param name="systemName" select="'CRM'"/>

    <!--Request/Response-->
    <xsl:param name="msgType" select="if (contains($rootElementName,'Rq') or contains($operationXsdSchema/@targetNamespace,'Rq')) then 'request' else 'response'"/>

    <!--В этой переменной идет выбор заголовка между разными системами. Сейчас выбор захорлкожен-->
    <!--!!! этот выбор захардкожен !!!-->
    <xsl:param name="headerType" select="if (/xsd:schema//xsd:element[@ref='kd4:KD4SoapHeaderV2']) then 'KD4' else 'NCP'"/>

    <!-- неймспейс, в котором будут содрежатся наши данные. Получается припиской к стандратному неймспейсу субпути /Data/ -->
    <xsl:variable name="targetNS" select="if ($operationXsdSchema/@targetNamespace) then mock:addDataToNamespaceUrl($operationXsdSchema/@targetNamespace, $rootElementName) else concat('http://sbrf.ru/mockService/',$rootElementName,'/Data/')"/>

    <!-- неймспейс, которым обозначается tns в targetNamespace -->
    <xsl:variable name="tnsAlias" select="$operationXsdSchema/namespace::*[.=$operationXsdSchema/@targetNamespace and string-length(local-name(.))>0]/local-name()"/>

    <!--списски констант-->
    <!--тэги, содержащие типы-->
    <xsl:variable name="xsdTagsToImport" select="tokenize('element complexType simpleType',' ')"/>
    <!--тэги, содержащие типы-->
    <xsl:variable name="atributesWithTypes" select="tokenize('ref base type',' ')"/>

    <!-- инклюды схем -->
    <xsl:variable name="includeFilesList" select="$operationXsdSchema/xsd:include/@schemaLocation"/>
    <xsl:variable name="includeFilesDocs" select="document($includeFilesList)/xsd:schema"/>

    <!-- импорт схем -->
    <xsl:variable name="importFilesList" select="$operationXsdSchema/xsd:import/@schemaLocation"/>
    <xsl:variable name="importFilesNs" select="$operationXsdSchema/xsd:import/@namespace"/>
    <xsl:variable name="importFilesNsAlias" select="$operationXsdSchema/namespace::*[.=$importFilesNs]/local-name()"/>
    <xsl:variable name="importFilesDocs" select="document($importFilesList)/xsd:schema"/>

    <xsl:variable name="typesList" select="$operationXsdSchema/*[local-name()=$xsdTagsToImport] | $includeFilesDocs/*[local-name()=$xsdTagsToImport] | $importFilesDocs/*[local-name()=$xsdTagsToImport]"/>

    <xsl:variable name="nsList" select="$operationXsdSchema/namespace::*[.!=$operationXsdSchema/@targetNamespace and string-length(local-name(.))>0]/string()"/>
    <xsl:variable name="nsAliasList" select="$operationXsdSchema/namespace::*[.!=$operationXsdSchema/@targetNamespace and string-length(local-name(.))>0]/local-name()"/>

    <xsl:variable name="DEBUG" select="false()"/>

    <!-- файл с темплейтом для soap header'а -->
    <xsl:include href="headerTemplates/headerTemplate.xsl"/>
    <xsl:include href="xsltFunctions.xsl"/>

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
        <xsl:variable name="importOnThislevel" select="($baseElement | $extensionElement)//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.)[not(contains(.,':'))]"/>
        <xsl:for-each select="$importOnThislevel"><xsl:value-of select="."/></xsl:for-each>
    </xsl:function>

    <xsl:template match="xsd:schema">
        <!--<xsl:comment>test <xsl:value-of select="$nsList"/></xsl:comment>-->
        <!--<xsl:comment>test <xsl:value-of select="$nsAliasList"/></xsl:comment>-->
        <xsl:element name="xsd:schema">
            <xsl:namespace name="" select="$targetNS"/>
            <xsl:namespace name="tns" select="$targetNS"/>
            <xsl:for-each select="$operationXsdSchema/namespace::*[.!=$operationXsdSchema/@targetNamespace and string-length(local-name(.))>0]">
                <xsl:namespace name="{local-name(.)}" select="."/>
            </xsl:for-each>
            <xsl:call-template name="headerNamespaces">
                <xsl:with-param name="headerType" select="$headerType"/>
            </xsl:call-template>
            <xsl:attribute name="targetNamespace">
                <xsl:value-of select="$targetNS"/>
            </xsl:attribute>
            <xsl:attribute name="elementFormDefault">qualified</xsl:attribute>
            <!--<xsl:for-each select="$operationXsdSchema/namespace::*[.!=$targetNS]">-->
            <!--<xsl:comment><xsl:value-of select="local-name(.)"/>=<xsl:value-of select="."/></xsl:comment>-->
            <!--</xsl:for-each>-->

            <xsl:apply-templates select="$operationXsdSchema/xsd:import" mode="imports"/>

            <!-- Заголовок из импортированного файла NCPSoapRqHeaderXSLTTemplate.xsl -->
            <xsl:call-template name="xsdHeader">
                <xsl:with-param name="headerType" select="$headerType"/>
            </xsl:call-template>

            <!-- корень данных -->
            <xsl:element name="xsd:element">
                <xsl:attribute name="name">data</xsl:attribute>
                <xsl:attribute name="type">dataValue</xsl:attribute>
            </xsl:element>

            <!-- контейнер ответов/запросов -->
            <xsl:element name="xsd:complexType">
                <xsl:attribute name="name">dataValue</xsl:attribute>
                <xsl:element name="xsd:sequence">
                    <xsl:element name="xsd:element">
                        <xsl:attribute name="name">
                            <xsl:value-of select="$msgType"/>
                        </xsl:attribute>
                        <xsl:attribute name="type">tns:<xsl:value-of select="$newDataNodeTypeName"/></xsl:attribute>
                        <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                        <xsl:element name="xsd:annotation">
                            <xsl:element name="xsd:documentation">Ответ на запрос</xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>

            <xsl:variable name="baseContainer" select="if (count($typesList[local-name()='complexType'][@name=$rootTypeName])>0) then ($typesList[local-name()='complexType'][@name=$rootTypeName])[1] else ($typesList[@name=$rootElementName])[1]"/>
            <xsl:if test="$DEBUG">
                <xsl:comment>test baseContainer <xsl:value-of select="$baseContainer/local-name()"/>-<xsl:value-of select="$baseContainer/@name"/></xsl:comment>
            </xsl:if>
            <xsl:apply-templates select="$baseContainer" mode="copyMainType"/>

        </xsl:element>
    </xsl:template>

    <!-- переносит если есть импорты из исходной xsd -->
    <xsl:template match="xsd:import" mode="imports">
        <xsl:element name="xsd:import">
            <xsl:attribute name="namespace">
                <xsl:value-of select="./@namespace"/>
            </xsl:attribute>
            <xsl:attribute name="schemaLocation">
                <xsl:text>../../../xsd/</xsl:text><xsl:value-of select="$systemName"/><xsl:text>/</xsl:text><xsl:value-of select="./@schemaLocation"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@type]" mode="copyMainType">
        <xsl:variable name="type" select="mock:removeNamespaceAlias(@type)"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:element[@type] <xsl:value-of select="$type"/></xsl:comment>
        </xsl:if>
        <xsl:apply-templates select="$typesList[@name=mock:removeNamespaceAlias($type)]" mode="copyMainType"/>
    </xsl:template>

    <xsl:template match="xsd:element[not(@type) and ./xsd:complexType]" mode="copyMainType">
        <xsl:variable name="name" select="mock:removeNamespaceAlias(@name)"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:element[not(@type)] <xsl:value-of select="$name"/></xsl:comment>
        </xsl:if>
        <xsl:element name="xsd:complexType">
            <xsl:copy-of select="./xsd:complexType/@*[not(./name()='name')]"/>
            <xsl:attribute name="name"><xsl:value-of select="$newDataNodeTypeName"/></xsl:attribute>
            <xsl:element name="xsd:sequence">
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">SoapHeader</xsl:attribute>
                    <xsl:attribute name="type">tns:Header</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                </xsl:element>
                <xsl:apply-templates select="./xsd:complexType/*" mode="copyMainTypeContainer"/>
            </xsl:element>
            <xsl:element name="xsd:attribute">
                <xsl:attribute name="name">name</xsl:attribute>
            </xsl:element>
        </xsl:element>

        <xsl:variable name="typesToImport" select="mock:typesToImport(./*)"/>
        <xsl:apply-templates select="$typesList[@name=$typesToImport]" mode="copyChildTypes"/>
    </xsl:template>

    <!-- перенос главного контейнера -->
    <xsl:template match="xsd:complexType" mode="copyMainType">
        <xsl:element name="xsd:complexType">
            <xsl:copy-of select="@*[not(./name()='name')]"/>
            <xsl:attribute name="name"><xsl:value-of select="$newDataNodeTypeName"/></xsl:attribute>
            <xsl:element name="xsd:sequence">
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">SoapHeader</xsl:attribute>
                    <xsl:attribute name="type">tns:Header</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                </xsl:element>
                <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            </xsl:element>
            <xsl:element name="xsd:attribute">
                <xsl:attribute name="name">name</xsl:attribute>
            </xsl:element>
        </xsl:element>

        <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
        <!--<xsl:variable name="typesToImport" select=".//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$tnsAlias)[not(contains(.,':'))]"/>-->
        <!--<xsl:comment>test <xsl:value-of select="$typesList"/></xsl:comment>-->
        <xsl:variable name="typesToImport" select="mock:typesToImport(./*)"/>
        <!--<xsl:comment>test2 <xsl:value-of select="$typesList[@name=$typesToImport]/@name"/></xsl:comment>-->
        <!--<xsl:comment>test3 <xsl:value-of select="mock:typesNeedingImportTest(./*)"/></xsl:comment>-->

        <xsl:apply-templates select="$typesList[@name=$typesToImport]" mode="copyChildTypes"/>

    </xsl:template>

    <!-- перенос содержимого главного контейнера -->
    <xsl:template match="xsd:extension" mode="copyMainTypeContainer">
        <xsl:variable name="name" select="mock:removeNamespaceAlias(@base)"/>
        <xsl:variable name="nsAlias" select="mock:getNamespaceAlias(@base)"/>
        <xsl:variable name="ns" select="namespace::*[local-name()=$nsAlias]"/>
        <!--<xsl:comment>test xsd:extension <xsl:value-of select="$name"/>;-->
        <!--<xsl:value-of select="$typesList[@name=$name]/../@targetNamespace"/></xsl:comment>-->
        <xsl:apply-templates select="$typesList[@name=$name and ./../@targetNamespace=$ns]" mode="copyMainTypeContainerextension"/>
        <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
    </xsl:template>

    <xsl:template match="xsd:sequence" mode="copyMainTypeContainer">
        <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>test xsd:sequence</xsl:comment>
        </xsl:if>
        <xsl:if test="count(child::*)=0">
            <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="xsd:element[@ref]" mode="copyMainTypeContainer">
        <xsl:variable name="refAlias" select="mock:getNamespaceAlias(@ref)"/>
        <xsl:variable name="refNamespace" select="ancestor::xsd:schema/namespace::*[local-name(.)=$refAlias]"/>
        <xsl:variable name="refName" select="mock:addAliasToName(mock:getAliasOfUrl($refNamespace),mock:removeNamespaceAlias(@ref))"/>
        <xsl:variable name="refNAName" select="mock:removeNamespaceAlias(@ref)"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>test xsd:element@ref</xsl:comment>
        </xsl:if>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:if test="not(./@name)">
                <xsl:attribute name="name"><xsl:value-of select="$refNAName"/></xsl:attribute>
            </xsl:if>
            <xsl:copy-of select="./@*[local-name(.)!='ref']"/>
            <xsl:attribute name="type"><xsl:value-of select="$refName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@base]" mode="copyMainTypeContainer">
        <xsl:variable name="baseAlias" select="mock:getNamespaceAlias(@base)"/>
        <xsl:variable name="baseNamespace" select="ancestor::xsd:schema/namespace::*[local-name(.)=$baseAlias]"/>
        <xsl:variable name="baseName" select="mock:addAliasToName(mock:getAliasOfUrl($baseNamespace),mock:removeNamespaceAlias(@base))"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>test xsd:element@base</xsl:comment>
        </xsl:if>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='base']"/>
            <xsl:attribute name="base"><xsl:value-of select="$baseName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@type]" mode="copyMainTypeContainer">
        <xsl:variable name="typeAlias" select="mock:getNamespaceAlias(@type)"/>
        <xsl:variable name="typeNamespace" select="ancestor::xsd:schema/namespace::*[local-name(.)=$typeAlias]"/>
        <xsl:variable name="typeName" select="mock:addAliasToName(mock:getAliasOfUrl($typeNamespace),mock:removeNamespaceAlias(@type))"/>
        <xsl:if test="$DEBUG">
            <xsl:comment>test xsd:element@type</xsl:comment>
        </xsl:if>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='type']"/>
            <xsl:attribute name="type"><xsl:value-of select="$typeName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[./xsd:complexType[not(@*)]/xsd:sequence]" mode="copyMainTypeContainer">
        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:element[./xsd:complexType[not(@*)]/xsd:sequence]</xsl:comment>
        </xsl:if>
        <xsl:element name="xsd:element">
            <xsl:copy-of select="./@*"/>
            <xsl:element name="xsd:complexType">
                <xsl:element name="xsd:sequence">
                    <xsl:apply-templates select="./xsd:complexType/xsd:sequence/*" mode="copyMainTypeContainer"/>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:complexContent" mode="copyMainTypeContainer">
        <xsl:if test="$DEBUG">
            <xsl:comment>test xsd:complexContent</xsl:comment>
        </xsl:if>
        <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
    </xsl:template>

    <xsl:template match="*" mode="copyMainTypeContainer">
        <xsl:if test="$DEBUG">
            <xsl:comment>test_copyMainTypeContainer_*</xsl:comment>
        </xsl:if>
        <xsl:if test="not(local-name(.)='annotation')">
            <xsl:element name="xsd:{local-name(.)}">
                <xsl:copy-of select="./@*"/>
                <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
                <xsl:if test="count(child::*)=0">
                    <xsl:value-of select="."/>
                </xsl:if>
            </xsl:element>
        </xsl:if>
    </xsl:template>

    <!-- копирование extension'а из главного типа -->

    <xsl:template match="xsd:complexType" mode="copyMainTypeContainerextension">
        <xsl:if test="$DEBUG">
            <xsl:comment>xsd:complexType</xsl:comment>
        </xsl:if>
        <xsl:apply-templates select="./*" mode="copyMainTypeContainerextension"/>
        <xsl:if test="count(child::*)=0">
            <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="*" mode="copyMainTypeContainerextension">
        <xsl:if test="not(local-name(.)='annotation')">
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template match="xsd:extension" mode="copyMainTypeContainerextension">
        <xsl:variable name="name" select="mock:removeNamespaceAlias(@base)"/>
        <xsl:variable name="nsAlias" select="mock:getNamespaceAlias(@base)"/>
        <xsl:variable name="ns" select="namespace::*[local-name()=$nsAlias]"/>
        <!--<xsl:comment>test xsd:extension <xsl:value-of select="$name"/>;-->
        <!--<xsl:value-of select="$typesList[@name=$name]/../@targetNamespace"/></xsl:comment>-->
        <xsl:apply-templates select="$typesList[@name=$name and ./../@targetNamespace=$ns]" mode="copyMainTypeContainerextension"/>
    </xsl:template>

    <xsl:template match="xsd:sequence" mode="copyMainTypeContainerextension">
        <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
    </xsl:template>

    <!--копирование остальных типов, встречающихся в дереве определения-->

    <xsl:template match="xsd:element" mode="copyChildTypes">
        <xsl:element name="xsd:complexType">
            <xsl:copy-of select="@*"/>
            <xsl:copy-of select="./xsd:complexType/@*"/>
            <xsl:apply-templates select="./*[local-name()!='complexType']" mode="copyTypeContainer"/>
            <xsl:apply-templates select="./xsd:complexType/*" mode="copyTypeContainer"/>
        </xsl:element>

        <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
        <!--<xsl:variable name="typesToImport" select=".//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$tnsAlias)[not(contains(.,':'))]"/>-->
        <!--<xsl:comment>test <xsl:value-of select="$typesToImport"/></xsl:comment>-->

        <!--<xsl:apply-templates select="$typesList[@name=$typesToImport]" mode="copyChildTypes"/>-->
    </xsl:template>


    <xsl:template match="xsd:complexType" mode="copyChildTypes">
        <xsl:element name="xsd:complexType">
            <xsl:copy-of select="@*"/>
            <xsl:if test="not (@name) and parent::*[local-name()='element']/@name">
                <xsl:attribute name="name"><xsl:value-of select="parent::*/@name"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:simpleType" mode="copyChildTypes">
        <xsl:element name="xsd:simpleType">
            <xsl:copy-of select="@*"/>
            <xsl:if test="not (@name) and parent::*[local-name()='element']/@name">
                <xsl:attribute name="name"><xsl:value-of select="parent::*/@name"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
        </xsl:element>
    </xsl:template>

    <!--копирование содержимого типов-->

    <xsl:template match="xsd:element[@ref]" mode="copyTypeContainer">
        <xsl:variable name="refAlias" select="mock:getNamespaceAlias(@ref)"/>
        <xsl:variable name="refNamespace" select="ancestor::xsd:schema/namespace::*[local-name(.)=$refAlias]"/>
        <xsl:variable name="refName" select="mock:addAliasToName(mock:getAliasOfUrl($refNamespace),mock:removeNamespaceAlias(@ref))"/>
        <xsl:variable name="refNAName" select="mock:removeNamespaceAlias(@ref)"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:if test="not(./@name)">
                <xsl:attribute name="name"><xsl:value-of select="$refNAName"/></xsl:attribute>
            </xsl:if>
            <xsl:copy-of select="./@*[local-name(.)!='ref']"/>
            <xsl:attribute name="type"><xsl:value-of select="$refName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:if test="$DEBUG">
                    <xsl:comment>xsd:element[@ref]</xsl:comment>
                </xsl:if>
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@base]" mode="copyTypeContainer">
        <xsl:variable name="baseAlias" select="mock:getNamespaceAlias(@base)"/>
        <xsl:variable name="baseNamespace" select="ancestor::xsd:schema/namespace::*[local-name(.)=$baseAlias]"/>
        <xsl:variable name="baseName" select="mock:addAliasToName(mock:getAliasOfUrl($baseNamespace),mock:removeNamespaceAlias(@base))"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='base']"/>
            <xsl:attribute name="base"><xsl:value-of select="$baseName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:if test="$DEBUG">
                    <xsl:comment>xsd:element[@base]</xsl:comment>
                </xsl:if>
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@type]" mode="copyTypeContainer">
        <xsl:variable name="typeAlias" select="mock:getNamespaceAlias(@type)"/>
        <xsl:variable name="typeNamespace" select="ancestor::xsd:schema/namespace::*[local-name(.)=$typeAlias]"/>
        <xsl:variable name="typeName" select="mock:addAliasToName(mock:getAliasOfUrl($typeNamespace),mock:removeNamespaceAlias(@type))"/>
        <!--<xsl:comment>test [<xsl:value-of select="$typeAlias"/>]:[<xsl:value-of select="$typeNamespace"/>]</xsl:comment>-->
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='type']"/>
            <xsl:attribute name="type"><xsl:value-of select="$typeName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:if test="$DEBUG">
                    <xsl:comment>xsd:element[@type]</xsl:comment>
                </xsl:if>
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:restriction[@base]" mode="copyTypeContainer">
        <xsl:variable name="baseAlias" select="mock:getNamespaceAlias(@base)"/>
        <xsl:variable name="baseNamespace" select="ancestor::xsd:schema/namespace::*[local-name(.)=$baseAlias]"/>
        <xsl:variable name="baseName" select="mock:addAliasToName(mock:getAliasOfUrl($baseNamespace),mock:removeNamespaceAlias(@base))"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='base']"/>
            <xsl:attribute name="base"><xsl:value-of select="$baseName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:if test="$DEBUG">
                    <xsl:comment>xsd:restriction[@base]</xsl:comment>
                </xsl:if>
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="*" mode="copyTypeContainer">
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*"/>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:if test="$DEBUG">
                    <xsl:comment>*</xsl:comment>
                </xsl:if>
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>