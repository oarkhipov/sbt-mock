<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsk="http://www.w3.org/1999/XSL/Transform"
                xmlns:mock="http://sbrf.ru/mockService">

    <!-- создание примера сообщения из xsd-файла. В прямую не должно работать, вызывается как инклюд из KD4SoapMsg.xsl или NCPSoapMSG.xsl-->
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
    <xsl:variable name="xsdNsAlias" select="$operationXsdSchema/namespace::*[.='http://www.w3.org/2001/XMLSchema']/local-name()[string-length()>0][1]"/>

    <!--нэймспейс-->
    <xsl:param name="targetNS" select="xsd:schema/@targetNamespace"/>
    <!--алиас неймспейса. Лучше не менрять-->
    <xsl:param name="targetNSAlias" select="'tns'"/>
    <!--алиас неймспейса, который используется в исходной xsd-->
    <xsl:param name="localTargetNSAlias" select="local-name(xsd:schema/namespace::*[.=$targetNS][string-length(local-name(.))>0])"/>
    <!--имя операции-->
    <xsl:param name="operation-name" select="$rootElementName"/>
    <!--показать дебаг сообщения -з нужно только для отладки -->
    <xsl:param name="debug" select="false()"/>

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

    <xsl:param name="typesDefinition" select="(//(xsd:complexType | xsd:simpleType)) | ($operationXsdSchema//(xsd:complexType | xsd:simpleType)) | ($importFilesDocs/(xsd:complexType | xsd:simpleType)) | ($includeFilesDocs/(xsd:complexType | xsd:simpleType))"/>
    <xsl:param name="elementsDefinition" select="(//(xsd:element)) | ($operationXsdSchema//(xsd:element)) | ($importFilesDocs/(xsd:element)) | ($includeFilesDocs/(xsd:element))"/>

    <!-- список известных типов-->
    <xsl:variable name="stringTypes" select="tokenize(concat('string xsd:string ',$xsdNsAlias,':string'),'\s+')"/>
    <xsl:variable name="digitTypes" select="tokenize(concat('int xsd:int ',$xsdNsAlias,':int integer xsd:integer ',$xsdNsAlias,':integer long xsd:long ',$xsdNsAlias,':long double xsd:double ',$xsdNsAlias,':double float xsd:float ',$xsdNsAlias,':float decimal xsd:decimal ',$xsdNsAlias,':decimal'),'\s+')"/>
    <xsl:variable name="dateTypes" select="tokenize(concat('date xsd:date ',$xsdNsAlias,':date'),'\s+')"/>
    <xsl:variable name="dateTimeTypes" select="tokenize(concat('dateTime xsd:dateTime ',$xsdNsAlias,':dateTime'),'\s+')"/>
    <xsl:variable name="booleanTypes" select="tokenize(concat('boolean xsd:boolean ',$xsdNsAlias,':boolean'),'\s+')"/>
    <xsl:variable name="anytypeTypes" select="tokenize(concat('anyType xsd:anyType ',$xsdNsAlias,':anyType'),'\s+')"/>

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

    <xsl:function name="mock:stringOfLenght">
        <xsl:param name="lenght"/>
        <xsl:variable name="pattern" select="'string'"/>
        <xsl:value-of select="substring($pattern, 1, $lenght)"/>
        <xsl:if test="number($lenght)-string-length($pattern)>0"><xsl:value-of select="mock:stringOfLenght(number($lenght)-string-length($pattern))"/></xsl:if>
    </xsl:function>

    <!--***********************************-->
    <!--******входной темплейт*************-->
    <!--***********************************-->

    <xsl:template match="xsd:complexType" mode="rootBodyElement">
        <xsl:param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
        <xsl:variable name="parrentNSAlias" select="if ($parrentNS=/xsd:schema/@targetNamespace) then $targetNSAlias else $systemName"/>
        <xsl:if test="$debug">
            <xsl:comment>complexType</xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>element</xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>tSubSq (<xsl:value-of select="$nsAlias"/>="<xsl:value-of select="$ns"/>")</xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>testxsdDigit <xsl:value-of select="$nsAlias"/>="<xsl:value-of select="$ns"/>"</xsl:comment>
        </xsl:if>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">10</xsl:element>
    </xsl:template>

    <!--обычные строки без ограничений-->
    <xsl:template match="xsd:element[@type=$stringTypes]" mode="type" priority="14">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testxsdString</xsl:comment>
        </xsl:if>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">string</xsl:element>
    </xsl:template>

    <!--дата-->
    <xsl:template match="xsd:element[@type=$dateTypes]" mode="type" priority="13">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testxsdDate</xsl:comment>
        </xsl:if>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}"><xsl:value-of select="format-date(current-date(),'[Y0001]-[M01]-[D01]')"/></xsl:element>
    </xsl:template>

    <!--дата-время-->
    <xsl:template match="xsd:element[@type=$dateTimeTypes]" mode="type" priority="12">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testxsdDateTime</xsl:comment>
        </xsl:if>
        <!--<xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"><xsl:value-of select="format-dateTime(current-dateTime(),'[D01].[M01].[Y0001]T[H01]:[m01]:[s01]')"/></xsl:element>-->
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}"><xsl:value-of select="format-dateTime(current-dateTime(),'[Y0001]-[M01]-[D01]T12:00:00')"/></xsl:element>
    </xsl:template>

    <!--буль-->
    <xsl:template match="xsd:element[@type=$booleanTypes]" mode="type" priority="11">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testxsdBool</xsl:comment>
        </xsl:if>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">false</xsl:element>
    </xsl:template>

    <!--enumeration-->
    <xsl:template match="xsd:element[./xsd:simpleType/xsd:restriction/xsd:enumeration]" mode="type" priority="1001">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testxsdEnum</xsl:comment>
        </xsl:if>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}"><xsl:value-of select=".//xsd:enumeration[1]/@value"/></xsl:element>
    </xsl:template>

    <!--простой тип унаследованнный от строки с рестриктом-->
    <xsl:template match="xsd:element[./xsd:simpleType/*[@base=$stringTypes]/xsd:maxLength]" mode="type" priority="1000">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testxsdStrMax</xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>testImport <xsl:value-of select="$typeName"/></xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>testImportBase <xsl:value-of select="$baseName"/>(<xsl:value-of select="$nsAlias"/>="<xsl:value-of select="$ns"/>)</xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>testXsd import {<xsl:value-of select="$typeLocalName"/>}</xsl:comment>
        </xsl:if>
        <!--<xsl:comment>testXsd import {<xsl:value-of select="//(xsd:complexType | xsd:simpleType)[@name=$typeLocalName]/@name"/>}</xsl:comment>-->
        <!--<xsl:comment>testXsd import {<xsl:value-of select="$typesList"/>}</xsl:comment>-->
        <!--<xsl:comment>testXsd import {<xsl:value-of select="$typesDefinition/@name"/>}</xsl:comment>-->
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
        <xsl:if test="$debug">
            <xsl:comment>testRef "<xsl:value-of select="$elementName"/>" base(<xsl:value-of select="$baseNsAlias"/>="<xsl:value-of select="$baseNs"/>)</xsl:comment>
            <xsl:comment>testRef <xsl:value-of select="$elementsDefinition/@name"/></xsl:comment>
        </xsl:if>
        <xsl:apply-templates select="$elementsDefinition[@name=$elementName]" mode="type">
            <xsl:with-param name="ns" select="$usedNs"/>
            <xsl:with-param name="nsAlias" select="$usedNsAlias"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--элемент с complexType-->
    <xsl:template match="xsd:element[./xsd:complexType]" mode="type"  priority="100">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testcomplexType</xsl:comment>
        </xsl:if>
        <xsl:element name="{concat($nsAlias,':',./@name)}" namespace="{$ns}">
            <xsl:apply-templates select="./xsd:complexType" mode="subeseq">
                <xsl:with-param name="ns" select="$ns"/>
                <xsl:with-param name="nsAlias" select="$nsAlias"/>
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>

    <!--элемент у которого никаких требований-->
    <xsl:template match="xsd:element[@type=$anytypeTypes
                            or (count(./*[local-name()!='annotation'])=0 and not(./@type) and not(./@ref))]" mode="type"  priority="10">
        <xsl:param name="ns" select="$targetNS"/>
        <xsl:param name="nsAlias" select="$targetNSAlias"/>
        <xsl:if test="$debug">
            <xsl:comment>testNoRestrictions</xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>testCT</xsl:comment>
        </xsl:if>
        <xsl:apply-templates select="." mode="subeseq">
            <xsl:with-param name="ns" select="$ns"/>
            <xsl:with-param name="nsAlias" select="$nsAlias"/>
        </xsl:apply-templates>
    </xsl:template>

    <!--тип decimal-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/@base=$digitTypes]" mode="importedType"  priority="13">
        <xsl:param name="params" select="./xsd:restriction/*"/>
        <xsl:if test="$debug">
            <xsl:comment>test6</xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>test6 <xsl:value-of select="$maxLength"/></xsl:comment>
        </xsl:if>
        <xsl:choose>
            <xsl:when test="$insideParams[./local-name()='length']">
                <xsl:variable name="lenght" select="number($insideParams[./local-name()='length'][1]/@value)"/>
                <xsl:value-of select="mock:stringOfLenght($lenght)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="substring($templateString, 1, $maxLength)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--date-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/@base=$dateTypes]" mode="importedType" priority="11">
        <xsl:if test="$debug">
            <xsl:comment>test7</xsl:comment>
        </xsl:if>
        <xsl:value-of select="format-date(current-date(),'[Y0001]-[M01]-[D01]')"/>
    </xsl:template>

    <!--dateTime-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/@base=$dateTimeTypes]" mode="importedType" priority="10">
        <xsl:if test="$debug">
            <xsl:comment>test7</xsl:comment>
        </xsl:if>
        <xsl:value-of select="format-dateTime(current-dateTime(),'[Y0001]-[M01]-[D01]T12:00:00')"/>
    </xsl:template>

    <!--enumeration-->
    <xsl:template match="xsd:simpleType[./xsd:restriction/xsd:enumeration]" mode="importedType"  priority="1000">
        <xsl:param name="params" select="./xsd:restriction/*"/>
        <xsl:variable name="insideParams" select="($params | ./xsd:restriction/*[not(./name()=$params/name())])"/>
        <xsl:if test="$debug">
            <xsl:comment>testEnumeration <xsl:value-of select="$insideParams/name()"/></xsl:comment>
        </xsl:if>
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
        <xsl:if test="$debug">
            <xsl:comment>test9 <xsl:value-of select="$typeName"/></xsl:comment>
        </xsl:if>
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
            <xsl:if test="$debug">
                <xsl:comment>test <xsl:value-of select="parent::*/namespace::*/local-name()"/></xsl:comment>
            </xsl:if>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>