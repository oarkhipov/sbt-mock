<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsk="http://www.w3.org/1999/XSL/Transform"
                xmlns:mock="http://sbrf.ru/mockService">

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <!--То что можно/нужно задать-->

    <xsl:param name="rootXSD" select="/xsd:schema"/>


    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/>
    <!--выкидываем ошибку, если нам не дали имя тэга элемента-->
    <xsl:variable name="throwError" select="if ($rootElementName!='') then true() else error(QName('http://sbrf.ru/mockService', 'err01'),'rootElementName not defined')"/>

    <!--система-->
    <xsl:param name="systemName" select="'CRM'"/>

    <!-- пропускать коменты. false - будут комментарии. true - не будет -->
    <xsl:param name="omitComments" select="'false'"/>
    <!-- при false все опциональные тэги будут пропущенны -->
    <xsl:param name="showOptionalTags" select="'true'"/>

    <!--Вставка значения для linkedTag. По умолчанию не используется (useLinkedTagValue=false). Иначе надо задать все 3 параметра -->
    <xsl:param name="useLinkedTagValue" select="'false'"/>
    <xsl:param name="tagNameToTakeLinkedTag"/>
    <xsl:param name="linkedTagValue" select="'test1'"/>

    <!--То, что задавать не нужно-->
    <!--алиас xsd схемы-->
    <xsl:param name="xsdNsAlias" select="local-name(xsd:schema/namespace::*[.='http://www.w3.org/2001/XMLSchema'])"/>
    <!--нэймспейс-->
    <xsl:param name="targetNS" select="xsd:schema/@targetNamespace"/>
    <!--алиас неймспейса. Лучше не менрять-->
    <xsl:param name="targetNSAlias" select="'tns'"/>
    <!--алиас неймспейса, который используется в исходной xsd-->
    <xsl:param name="localTargetNSAlias" select="local-name(xsd:schema/namespace::*[.=$targetNS][string-length(local-name(.))>0])"/>
    <!--имя операции-->
    <xsl:param name="operation-name" select="$rootElementName"/>

    <!-- инклюды схем -->
    <xsl:param name="includeFilesList" select="xsd:schema/xsd:include/@schemaLocation"/>
    <xsl:param name="includeFilesDocs" select="document($includeFilesList)/xsd:schema"/>

    <!-- импорт схем -->
    <xsl:param name="importFilesList" select="xsd:schema/xsd:import/@schemaLocation"/>
    <xsl:param name="importFilesNs" select="xsd:schema/xsd:import/@namespace"/>
    <xsl:param name="importFilesNsAlias" select="xsd:schema/namespace::*[.=$importFilesNs]/local-name()"/>
    <xsl:param name="importFilesDocs" select="document($importFilesList)/xsd:schema"/>

    <!--список всех типов, которые объявленны в схеме-->
    <xsl:param name="typesList" select="(//(xsd:complexType | xsd:simpleType)/@name) | ($importFilesDocs/(xsd:complexType | xsd:simpleType)/@name) | ($includeFilesDocs/(xsd:complexType | xsd:simpleType)/@name)"/>

    <xsl:param name="typesDefinition" select="(//(xsd:complexType | xsd:simpleType)) | ($importFilesDocs/(xsd:complexType | xsd:simpleType)) | ($includeFilesDocs/(xsd:complexType | xsd:simpleType))"/>


    <!-- список известных типов-->
    <xsl:variable name="stringTypes" select="tokenize('string xsd:string','\s+')"/>
    <xsl:variable name="digitTypes" select="tokenize('int xsd:int integer xsd:integer long xsd:long double xsd:double float xsd:float decimal xsd:decimal','\s+')"/>
    <xsl:variable name="dateTypes" select="tokenize('date xsd:date','\s+')"/>
    <xsl:variable name="dateTimeTypes" select="tokenize('dateTime xsd:dateTime','\s+')"/>
    <xsl:variable name="booleanTypes" select="tokenize('boolean xsd:boolean','\s+')"/>

    <!--***********************************-->
    <!--************Функции****************-->
    <!--***********************************-->

    <xsl:function name="mock:removeNamespaceAlias">
        <!--Убрать из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'ClientReferenceData'-->
        <xsl:param name="string"/>
        <xsl:value-of select="replace($string, '^([\w_]+:)?([\w_]+)$', '$2')"/>
    </xsl:function>

    <xsl:function name="mock:getNamespaceAlias">
        <!--Убрать из строки имя неймспейса. Например 'tns:ClientReferenceData' в 'ClientReferenceData'-->
        <xsl:param name="string"/>
        <xsl:value-of select="replace($string, '^(([\w_]+):)?([\w_]+)$', '$2')"/>
    </xsl:function>

    <!--***********************************-->
    <!--******входной темплейт*************-->
    <!--***********************************-->

    <xsl:template match="xsd:complexType" mode="rootBodyElement">
        <xsl:param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
        <xsl:variable name="parrentNSAlias" select="if ($parrentNS=/xsd:schema/@targetNamespace) then $targetNSAlias else $systemName"/>
        <!--<xsl:comment>complexType</xsl:comment>-->
        <xsl:element name="{concat($parrentNSAlias,':',$rootElementName)}" namespace="{$parrentNS}">
            <xsl:if test="$parrentNS!=''">
                <xsl:namespace name="{$parrentNSAlias}" select="$parrentNS"/>
                <xsl:namespace name="{$targetNSAlias}" select="$targetNS"/>
            </xsl:if>
            <xsl:apply-templates select="./xsd:sequence/xsd:element" mode="subelement"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element" mode="rootBodyElement">
        <xsl:param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
        <xsl:variable name="parrentNSAlias" select="if ($parrentNS=/xsd:schema/@targetNamespace) then $targetNSAlias else $systemName"/>
        <!--<xsl:comment>element</xsl:comment>-->
        <xsl:element name="{concat($parrentNSAlias,':',$rootElementName)}" namespace="{$parrentNS}">
            <xsl:if test="$parrentNS!=''">
                <xsl:namespace name="{$parrentNSAlias}" select="$parrentNS"/>
            </xsl:if>
            <xsl:if test="$targetNS!=''">
                <xsl:namespace name="{$targetNSAlias}" select="$targetNS"/>
            </xsl:if>
            <xsl:if test="./xsd:complexType/xsd:complexContent/xsd:extension/@base">
                <xsl:variable name="baseLocalName" select="mock:removeNamespaceAlias(./xsd:complexType/xsd:complexContent/xsd:extension/@base)"/>
                <xsl:variable name="baseNsAlias" select="mock:getNamespaceAlias(./xsd:complexType/xsd:complexContent/xsd:extension/@base)"/>
                <xsl:variable name="baseNs" select="namespace::*[local-name()=$baseNsAlias]"/>
                <xsl:variable name="usedNsAlias" select="if ($baseNs=$targetNS) then $targetNSAlias else
                                                if(string-length($baseNsAlias)>0) then $baseNsAlias else $targetNSAlias"/>
                <!--<xsl:comment>testrootBodyElementExtension (<xsl:value-of select="$usedNsAlias"/>="<xsl:value-of select="$baseNs"/>)</xsl:comment>-->
                <xsl:apply-templates select="$typesDefinition[@name=$baseLocalName]" mode="subeseq">
                    <xsl:with-param name="ns" select="$baseNs"/>
                    <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
                </xsl:apply-templates>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="@type">
                    <xsl:variable name="typeLocalName" select="mock:removeNamespaceAlias(@type)"/>
                    <!--<xsl:comment>test <xsl:value-of select="$typeLocalName"/></xsl:comment>-->
                    <!--<xsl:comment>test <xsl:value-of select="//xsd:complexType[@name=$typeLocalName]/@name"/></xsl:comment>-->
                    <xsl:apply-templates select="$typesDefinition[@name=$typeLocalName]" mode="subeseq"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select=".//xsd:sequence/xsd:element" mode="subelement"/> <!--TODO этот запрос может вжать лишние подэлементы - элементы внутри типов элементов -->
                </xsl:otherwise>
            </xsl:choose>
        </xsl:element>
    </xsl:template>

    <!--сложный тип, в котором содержатся еще элемениты-->
    <xsl:template match="xsd:complexType" mode="subeseq">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>tSubSq (<xsl:value-of select="$nsAlias"/>="<xsl:value-of select="$ns"/>")</xsl:comment>-->
        <xsl:if test="./xsd:complexContent/xsd:extension">
            <xsl:variable name="baseLocalName" select="mock:removeNamespaceAlias(./xsd:complexContent/xsd:extension/@base)"/>
            <xsl:variable name="baseNsAlias" select="mock:getNamespaceAlias(./xsd:complexContent/xsd:extension/@base)"/>
            <xsl:variable name="baseNs" select="namespace::*[local-name()=$baseNsAlias]"/>
            <xsl:variable name="usedNsAlias" select="if ($baseNs=$targetNS) then $targetNSAlias else
                                            if ($baseNs=$ns) then $nsAlias else
                                                if(string-length($baseNsAlias)>0) then $baseNsAlias else $nsAlias"/>
            <xsl:apply-templates select="$typesDefinition[@name=$baseLocalName]" mode="subeseq">
                <xsl:with-param name="ns" select="$baseNs"/>
                <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
            </xsl:apply-templates>
        </xsl:if>
        <xsl:apply-templates select="
        ./xsd:sequence/xsd:element
        | ./xsd:complexContent/xsd:sequence/xsd:element
        | ./xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
        " mode="subelement">
            <xsl:with-param name="ns" select="$ns"/>
            <xsl:with-param name="nsAlias" select="$nsAlias"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--***********************************-->
    <!-- матчи элементов по колличеству - после определения вызываем новый матч в режиме определения типа -->
    <!--***********************************-->

    <xsl:template match="xsd:element[not(@minOccurs) and not(@maxOccurs)]" mode="subelement"
                  priority="3">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:apply-templates select="self::*" mode="type">
            <xsl:with-param name="ns" select="$ns"/>
            <xsl:with-param name="nsAlias" select="$nsAlias"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="xsd:element[@minOccurs=0 and not(@maxOccurs)]" mode="subelement"
                  priority="2">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$showOptionalTags='true'">
            <xsl:if test="$omitComments">
                <xsl:comment>optional</xsl:comment>
            </xsl:if>
            <xsl:apply-templates select="self::*" mode="type">
                <xsl:with-param name="ns" select="$ns"/>
                <xsl:with-param name="nsAlias" select="$nsAlias"/>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <xsl:template match="xsd:element" mode="subelement"
                  priority="1">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:variable name="min" select="if(@minOccurs) then @minOccurs else 1"/>
        <xsl:variable name="max" select="if(@maxOccurs) then @maxOccurs else $min"/>
        <xsl:if test="$showOptionalTags='true' or $min>0">
            <xsl:if test="$omitComments">
                <xsl:comment>from <xsl:value-of select="$min"/> to <xsl:value-of select="$max"/> elements </xsl:comment>
            </xsl:if>
            <xsl:apply-templates select="self::*" mode="type">
                <xsl:with-param name="ns" select="$ns"/>
                <xsl:with-param name="nsAlias" select="$nsAlias"/>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <!--заполняем значение линкедТага, но только если попросят-->
    <xsl:template match="xsd:element[$useLinkedTagValue='true' and $tagNameToTakeLinkedTag!='null' and @name = $tagNameToTakeLinkedTag]" mode="subelement"
                  priority="4">
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"><xsl:value-of select="$linkedTagValue"/></xsl:element>
    </xsl:template>

    <!--***********************************-->
    <!-- матчи элеметнов по типу-->
    <!--***********************************-->

    <!--обычные числа-->
    <xsl:template match="xsd:element[(@type=$digitTypes) or (./xsd:simpleType/xsd:restriction/@base=$digitTypes)]" mode="type" priority="15">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testxsdDigit <xsl:value-of select="$nsAlias"/>="<xsl:value-of select="$ns"/>"</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">10</xsl:element>
    </xsl:template>

    <!--обычные строки без ограничений-->
    <xsl:template match="xsd:element[@type=$stringTypes]" mode="type" priority="14">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testxsdString</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">string</xsl:element>
    </xsl:template>

    <!--дата-->
    <xsl:template match="xsd:element[@type=$dateTypes]" mode="type" priority="13">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testxsdDate</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}"><xsl:value-of select="format-date(current-date(),'[Y0001]-[M01]-[D01]')"/></xsl:element>
    </xsl:template>

    <!--дата-время-->
    <xsl:template match="xsd:element[@type=$dateTimeTypes]" mode="type" priority="12">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testxsdDateTime</xsl:comment>-->
        <!--<xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"><xsl:value-of select="format-dateTime(current-dateTime(),'[D01].[M01].[Y0001]T[H01]:[m01]:[s01]')"/></xsl:element>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}"><xsl:value-of select="format-dateTime(current-dateTime(),'[Y0001]-[M01]-[D01]T12:00:00')"/></xsl:element>
    </xsl:template>

    <!--буль-->
    <xsl:template match="xsd:element[@type=$booleanTypes]" mode="type" priority="11">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testxsdBool</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">false</xsl:element>
    </xsl:template>

    <!--enumeration-->
    <xsl:template match="xsd:element[./xsd:simpleType/xsd:restriction/xsd:enumeration]" mode="type" priority="1001">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testxsdEnum</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}"><xsl:value-of select=".//xsd:enumeration[1]/@value"/></xsl:element>
    </xsl:template>

    <!--простой тип унаследованнный от строки с рестриктом-->
    <xsl:template match="xsd:element[./xsd:simpleType/*[@base=$stringTypes]/xsd:maxLength]" mode="type" priority="1000">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testxsdStrMax</xsl:comment>-->
        <xsl:variable name="maxlen" select="./xsd:simpleType/*[@base=$stringTypes]/xsd:maxLength/@value"/>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">
            <xsl:value-of select="substring('string', 1, $maxlen)"/>
        </xsl:element>
    </xsl:template>

    <!--элемент с типом, определенным в импротах-->
    <xsl:template match="xsd:element[mock:getNamespaceAlias(./@type)=$importFilesNsAlias]" mode="type" priority="99">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:variable name="typeName" select="mock:removeNamespaceAlias(./@type)"/>
        <xsl:variable name="baseNsAlias" select="mock:getNamespaceAlias(./@type)"/>
        <xsl:variable name="baseNs" select="namespace::*[local-name()=$nsAlias]"/>
        <xsl:variable name="usedNsAlias" select="if ($baseNs=$targetNS) then $targetNSAlias else
                                            if ($baseNs=$ns) then $nsAlias else
                                                if(string-length($baseNsAlias)>0) then $baseNsAlias else $nsAlias"/>
        <xsl:variable name="usedNs" select="$baseNs"/>
        <!--<xsl:comment>testImport <xsl:value-of select="$typeName"/></xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">
            <xsl:apply-templates select="$typesDefinition[@name=$typeName]" mode="importedType">
                <xsl:with-param name="ns" select="$usedNs"/>
                <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>

    <!--элемент с простым базовым классом, определенным в импротах. похож на темплейт выше-->
    <xsl:template match="xsd:element[mock:getNamespaceAlias(./xsd:simpleType/xsd:restriction[not(./xsd:enumeration)]/@base)=$importFilesNsAlias]"
                  mode="type"   priority="103">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:variable name="baseName" select="mock:removeNamespaceAlias(./xsd:simpleType/xsd:restriction/@base)"/>
        <xsl:variable name="baseNsAlias" select="mock:getNamespaceAlias(./xsd:simpleType/xsd:restriction/@base)"/>
        <xsl:variable name="baseNs" select="namespace::*[local-name()=$baseNsAlias]"/>
        <xsl:variable name="usedNsAlias" select="if ($baseNs=$targetNS) then $targetNSAlias else
                                            if ($baseNs=$ns) then $nsAlias else
                                                if(string-length($baseNsAlias)>0) then $baseNsAlias else $nsAlias"/>
        <xsl:variable name="usedNs" select="$baseNs"/>
        <!--<xsl:comment>testImportBase <xsl:value-of select="$baseName"/>(<xsl:value-of select="$nsAlias"/>="<xsl:value-of select="$ns"/>)</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">
            <xsl:apply-templates select="$typesDefinition[@name=$baseName]" mode="importedType">
                <xsl:with-param name="ns" select="$usedNs"/>
                <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>

    <!--элемент с атрибутом @type, который есть в xsd-->
    <xsl:template match="xsd:element[mock:removeNamespaceAlias(./@type)= $typesList]" mode="type"  priority="102">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:variable name="typeLocalName" select="mock:removeNamespaceAlias(./@type)"/>
        <xsl:variable name="baseNsAlias" select="mock:getNamespaceAlias(./@type)"/>
        <xsl:variable name="baseNs" select="namespace::*[local-name()=$baseNsAlias]"/>
        <xsl:variable name="usedNsAlias" select="if ($baseNs=$targetNS) then $targetNSAlias else
                                            if ($baseNs=$ns) then $nsAlias else
                                                if(string-length($baseNsAlias)>0) then $baseNsAlias else $nsAlias"/>
        <xsl:variable name="usedNs" select="$baseNs"/>
        <!--<xsl:comment>testXsd import {<xsl:value-of select="$typeLocalName"/>}</xsl:comment>-->
        <!--<xsl:comment>testXsd import {<xsl:value-of select="//(xsd:complexType | xsd:simpleType)[@name=$typeLocalName]/@name"/>}</xsl:comment>-->
        <!--<xsl:comment>testXsd import {<xsl:value-of select="$typesList"/>}</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">
            <xsl:apply-templates select="$typesDefinition[@name=$typeLocalName]" mode="importedType">
                <xsl:with-param name="ns" select="$usedNs"/>
                <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>

    <!--элемент с атрибутом @ref. Берется из импортированных схем-->
    <xsl:template match="xsd:element[@ref]" mode="type"  priority="101">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:param name="elementName" select="mock:removeNamespaceAlias(./@ref)"/>
        <xsl:variable name="baseNsAlias" select="mock:getNamespaceAlias(./@ref)"/>
        <xsl:variable name="baseNs" select="namespace::*[local-name()=$baseNsAlias]"/>
        <xsl:variable name="usedNsAlias" select="if ($baseNs=$targetNS) then $targetNSAlias else
                                            if ($baseNs=$ns) then $nsAlias else
                                                if(string-length($baseNsAlias)>0) then $baseNsAlias else $nsAlias"/>
        <xsl:variable name="usedNs" select="$baseNs"/>
        <!--<xsl:comment>testRef base(<xsl:value-of select="$baseNsAlias"/>="<xsl:value-of select="$baseNs"/>)</xsl:comment>-->
        <!--<xsl:comment>testRef (<xsl:value-of select="$usedNsAlias"/>="<xsl:value-of select="$usedNs"/>)</xsl:comment>-->
        <xsl:apply-templates select="$includeFilesDocs/xsd:element[@name=$elementName] | $importFilesDocs/xsd:element[@name=$elementName]" mode="type">
            <xsl:with-param name="ns" select="$usedNs"/>
            <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--элемент с complexType-->
    <xsl:template match="xsd:element[./xsd:complexType]" mode="type"  priority="100">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testcomplexType</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">
            <xsl:apply-templates select="./xsd:complexType" mode="subeseq">
                <xsl:with-param name="ns" select="$ns"/>
                <xsl:with-param name="nsAlias" select="$nsAlias"/>
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>

    <!--элемент у которого никаких требований-->
    <xsl:template match="xsd:element[@type='xsd:anyType'
                            or (count(./*[local-name()!='annotation'])=0 and not(./@type) and not(./@ref))]" mode="type"  priority="10">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testNoRestrictions</xsl:comment>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">anyString</xsl:element>
    </xsl:template>

    <!--***********************************-->
    <!-- если не нашли матч - пишем об этом комментарий -->
    <xsl:template match="xsd:element" mode="type" priority="1">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$omitComments">
            <xsl:comment>not known elementType:{<xsl:value-of select="@type"/>}</xsl:comment>
            <!--<xsl:comment><xsl:value-of select="mock:getNamespaceAlias(./@type)"/> (<xsl:value-of select="./@type"/>) from <xsl:value-of select="$importFilesNsAlias"/></xsl:comment>-->
            <!--<xsl:comment>t1:-->
                <!--<xsl:value-of select="$typesList"/></xsl:comment>-->
            <!--<xsl:comment>t2:-->
                <!--<xsl:value-of select="//(xsd:complexType | xsd:simpleType)/@name"/></xsl:comment>-->
            <!--<xsl:comment>t3:-->
                <!--<xsl:value-of select="//(xsd:complexType | xsd:simpleType)/@name | $importFilesDocs/(xsd:complexType | xsd:simpleType)/@name | $includeFilesDocs/(xsd:complexType | xsd:simpleType)/@name"/></xsl:comment>-->
            <!--<xsl:comment>t2:<xsl:value-of select="$includeFilesDocs/xsd:complexType/@name"/></xsl:comment>-->

        </xsl:if>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}"/>
    </xsl:template>


    <!--***********************************-->
    <!-- разбор импортированных типов -->
    <!--***********************************-->

    <!--сложный тип-->
    <xsl:template match="xsd:complexType" mode="importedType"  priority="2">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <!--<xsl:comment>testCT</xsl:comment>-->
        <xsl:apply-templates select="." mode="subeseq">
            <xsl:with-param name="ns" select="$ns"/>
            <xsl:with-param name="nsAlias" select="$nsAlias"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--тип decimal-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/@base=$digitTypes]" mode="importedType"  priority="13">
        <xsl:param name="params" select="./xsd:restriction/*"/>
        <!--<xsl:comment>test6</xsl:comment>-->
        <xsl:variable name="insideParams" select="($params | ./xsd:restriction/*[not(./name()=$params/name())])"/>
        <xsl:variable name="totalDigits" select="if ($insideParams[./local-name()='totalDigits']) then number($insideParams[./local-name()='totalDigits']/@value) else number('9999')"/>
        <!--TODO рассмотреть случай с minLengh -->
        <xsl:variable name="templateString" select="if ($insideParams[./local-name()='maxInclusive']) then ($insideParams[./local-name()='maxInclusive']/@value)[1] else '1234567890'"/>
        <xsl:value-of select="substring($templateString, 1, $totalDigits)"/>
    </xsl:template>

    <!--строка-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/@base=$stringTypes]" mode="importedType"  priority="12">
        <xsl:param name="params" select="./xsd:restriction/*"/>
        <xsl:variable name="insideParams" select="($params | ./xsd:restriction/*[not(./name()=$params/name())])"/>
        <xsl:variable name="maxLength" select="if ($insideParams[./local-name()='maxLength']) then number($insideParams[./local-name()='maxLength']/@value) else number('9999')"/>
        <!--TODO рассмотреть случай с minLengh -->
        <xsl:variable name="templateString" select="if($insideParams[./local-name()='pattern']) then '1234567890'  else 'string'"/>
        <!--<xsl:comment>test6 <xsl:value-of select="$maxLength"/></xsl:comment>-->
        <xsl:value-of select="substring($templateString, 1, $maxLength)"/>
    </xsl:template>

    <!--date-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/@base=$dateTypes]" mode="importedType" priority="11">
        <!--<xsl:comment>test7</xsl:comment>-->
        <xsl:value-of select="format-date(current-date(),'[Y0001]-[M01]-[D01]')"/>
    </xsl:template>

    <!--dateTime-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/@base=$dateTimeTypes]" mode="importedType" priority="10">
        <!--<xsl:comment>test7</xsl:comment>-->
        <xsl:value-of select="format-dateTime(current-dateTime(),'[Y0001]-[M01]-[D01]T12:00:00')"/>
    </xsl:template>

    <!--enumeration-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/xsd:enumeration]" mode="importedType"  priority="1000">
        <xsl:param name="params" select="./xsd:restriction/*"/>
        <xsl:variable name="insideParams" select="($params | ./xsd:restriction/*[not(./name()=$params/name())])"/>
        <!--<xsl:comment>testEnumeration <xsl:value-of select="$insideParams/name()"/></xsl:comment>-->
        <xsl:value-of select="$insideParams[./local-name()='enumeration'][1]/@value"/>
    </xsl:template>

    <!--ссылка на другой тип с дополнительными рестриктами-->
    <xsl:template match="xsd:simpleType[mock:getNamespaceAlias(./xsd:restriction/@base)=parent::*/namespace::*/local-name()
                                and (mock:removeNamespaceAlias(./xsd:restriction/@base)=$importFilesDocs/xsd:*/@name)
                                ] " mode="importedType" priority="100">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:param name="params" select="./xsd:restriction/*"/>
        <xsl:variable name="insideParams" select="($params | ./xsd:restriction/*[not(./name()=$params/name())])"/>
        <xsl:variable name="typeName" select="mock:removeNamespaceAlias(./xsd:restriction/@base)"/>
        <!--<xsl:comment>test9 <xsl:value-of select="$typeName"/></xsl:comment>-->
        <xsl:apply-templates select="$typesDefinition[@name=$typeName]" mode="importedType">
            <xsl:with-param name="params" select="$insideParams"/>
            <xsl:with-param name="ns" select="$ns"/>
            <xsl:with-param name="nsAlias" select="$nsAlias"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--***********************************-->
    <!-- если не нашли матч - пишем об этом комментарий -->
    <xsl:template match="*" mode="importedType" priority="1">
        <xsl:if test="$omitComments">
            <xsl:comment>not known type <xsl:value-of select="./xsd:restriction/@base"/></xsl:comment>
            <!--<xsl:comment>test <xsl:value-of select="parent::*/namespace::*/local-name()"/></xsl:comment>-->
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>