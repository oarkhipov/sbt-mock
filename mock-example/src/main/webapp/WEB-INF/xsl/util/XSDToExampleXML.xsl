<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema">

    <xsl:import href="NCPSoapRqHeaderXSLTTemplate.xsl"/>

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <!--То что можно/нужно задать-->
    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <!-- TODO выбрать этот параметр более надежным способом -->
    <xsl:param name="entryPointName" select="replace(xsd:schema/@targetNamespace,'^.+/(\w+)(/[0-9\.]+)?/$','$1')"/>
    <!--схема рут-элемента транзакции-->
    <xsl:param name="parrentNS" select="'http://sbrf.ru/NCP/CRM/'"/>
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
    <!--список всех типов, которые объявленны в схеме-->
    <xsl:param name="typesList" select="//xsd:complexType/@name"/>
    <!--имя операции-->
    <xsl:param name="operation-name" select="$entryPointName"/>

    <xsl:variable name="CommonTypesNSAlias" select="local-name(xsd:schema/namespace::*[contains(.,'CommonTypes')])"/> <!-- алиас для xsd библиотеки типов CommonTypes. нужен потому что отличается от файла к файлу -->

    <xsl:template match="xsd:schema">
        <xsl:element name="soap-env:Envelope">
            <!--<xsl:comment><xsl:value-of select="$CommonTypesNSAlias"/></xsl:comment>-->
            <xsl:call-template name="NCPHeaderExample">
                <xsl:with-param name="operation-name" select="$operation-name"/>
            </xsl:call-template>
            <xsl:element name="soap-env:Body">
                <xsl:apply-templates select="./xsd:complexType[@name=$entryPointName]" mode="rootBodyElement"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:complexType" mode="rootBodyElement">
        <xsl:element name="{concat($systemName,':',./@name)}" namespace="{$parrentNS}">
            <xsl:namespace name="{$systemName}" select="$parrentNS"/>
            <xsl:namespace name="{$targetNSAlias}" select="$targetNS"/>
            <xsl:apply-templates select="./xsd:sequence/xsd:element" mode="subelement"/>
        </xsl:element>
    </xsl:template>

    <!--сложный тип, в котором содержатся еще элемениты-->
    <xsl:template match="xsd:complexType" mode="subeseq">
        <xsl:apply-templates select="./xsd:sequence/xsd:element" mode="subelement"/>
    </xsl:template>

    <!-- матчи элементов по колличеству - после определения вызываем новый матч в режиме определения типа -->

    <xsl:template match="xsd:element[not(@minOccurs) and not(@maxOccurs)]" mode="subelement"
                  priority="3">
        <xsl:apply-templates select="self::*" mode="type"/>
    </xsl:template>

    <xsl:template match="xsd:element[@minOccurs=0 and not(@maxOccurs)]" mode="subelement"
                  priority="2">
        <xsl:if test="$showOptionalTags='true'">
            <xsl:if test="$omitComments">
                <xsl:comment>optional</xsl:comment>
            </xsl:if>
            <xsl:apply-templates select="self::*" mode="type"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="xsd:element" mode="subelement"
            priority="1">
        <xsl:variable name="min" select="if(@minOccurs) then @minOccurs else 1"/>
        <xsl:variable name="max" select="if(@maxOccurs) then @maxOccurs else $min"/>
        <xsl:if test="$showOptionalTags='true' or $min>0">
            <xsl:if test="$omitComments">
                <xsl:comment>from <xsl:value-of select="$min"/> to <xsl:value-of select="$max"/> elements </xsl:comment>
            </xsl:if>
            <xsl:apply-templates select="self::*" mode="type"/>
        </xsl:if>
    </xsl:template>

    <!--заполняем значение линкедТага, но только если попросят-->
    <xsl:template match="xsd:element[$useLinkedTagValue='true' and $tagNameToTakeLinkedTag!='null' and @name = $tagNameToTakeLinkedTag]" mode="subelement"
            priority="4">
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"><xsl:value-of select="$linkedTagValue"/></xsl:element>
    </xsl:template>

    <!-- матчи элеметнов по типу-->

    <!--обычные числа-->
    <xsl:template match="xsd:element[@type='xsd:decimal' or @type='xsd:int' or @type='xsd:double']" mode="type">
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">10</xsl:element>
    </xsl:template>

    <!--обычне строки без ограничений-->
    <xsl:template match="xsd:element[@type='xsd:anyType' or @type='xsd:string']" mode="type">
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">string</xsl:element>
    </xsl:template>

    <!--дата-->
    <xsl:template match="xsd:element[@type='xsd:date']" mode="type">
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"><xsl:value-of select="format-date(current-date(),'[Y0001]-[M01]-[D01]')"/></xsl:element>
    </xsl:template>

    <!--дата-время-->
    <xsl:template match="xsd:element[@type='xsd:dateTime']" mode="type">
        <!--<xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"><xsl:value-of select="format-dateTime(current-dateTime(),'[D01].[M01].[Y0001]T[H01]:[m01]:[s01]')"/></xsl:element>-->
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"><xsl:value-of select="format-dateTime(current-dateTime(),'[Y0001]-[M01]-[D01]T12:00:00')"/></xsl:element>
    </xsl:template>

    <!--буль-->
    <xsl:template match="xsd:element[@type='xsd:boolean']" mode="type">
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">false</xsl:element>
    </xsl:template>

    <!--простой тип унаследованнный от строки с рестриктом-->
    <xsl:template match="xsd:element[./xsd:simpleType/*[@base='xsd:string']/xsd:maxLength]" mode="type">
        <xsl:variable name="maxlen" select="./xsd:simpleType/*[@base='xsd:string']/xsd:maxLength/@value"/>
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">
            <xsl:value-of select="substring('string', 1, $maxlen)"/>
        </xsl:element>
    </xsl:template>

    <!--строка с ограничением из CommonTypes-->
    <xsl:template match="xsd:element[string-length($CommonTypesNSAlias)>0 and contains(@type, concat($CommonTypesNSAlias,':string'))]" mode="type">
        <xsl:variable name="maxlen" select="number(replace(./@type, concat($CommonTypesNSAlias,':string'), ''))"/>
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">
            <xsl:value-of select="substring('string', 1, $maxlen)"/>
        </xsl:element>
    </xsl:template>

    <!--число с ограничением из CommonTypes-->
    <xsl:template match="xsd:element[string-length($CommonTypesNSAlias)>0 and contains(@type, concat($CommonTypesNSAlias,':integer'))]" mode="type">
        <xsl:variable name="maxlen" select="number(replace(./@type, concat($CommonTypesNSAlias,':integer'), ''))"/>
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">
            <xsl:value-of select="substring('1234567890', 1, $maxlen)"/>
        </xsl:element>
    </xsl:template>

    <!--число с плавающей точкой-->
    <xsl:template match="xsd:element[string-length($CommonTypesNSAlias)>0 and contains(@type, concat($CommonTypesNSAlias,':double'))]" mode="type">
        <xsl:variable name="maxlen" select="replace(./@type, concat($CommonTypesNSAlias,':double'), '')"/>
        <xsl:variable name="num" select="number(replace($maxlen, '(\d+)_(\d+)', '$1'))"/>
        <xsl:variable name="dotnum" select="number(replace($maxlen, '(\d+)_(\d+)', '$2'))"/>
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">
            <xsl:value-of select="substring('1234567890', 1, $num)"/>.<xsl:value-of select="substring('1234567890', 1, $dotnum)"/>
        </xsl:element>
    </xsl:template>

    <!--<xsl:template match="xsd:element[contains($typesList,replace(@type,concat($localTargetNSAlias,':'),''))]" mode="type">-->
    <xsl:template match="xsd:element[replace(@type,concat($localTargetNSAlias,':'),'')= $typesList]" mode="type">
        <xsl:variable name="typeLocalName" select="replace(@type,concat($localTargetNSAlias,':'),'')"/>
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}">
            <xsl:apply-templates select="//xsd:complexType[@name=$typeLocalName]" mode="subeseq"/>
        </xsl:element>
    </xsl:template>

    <!-- если не нашли матч - пишем об этом комментарий -->
    <xsl:template match="xsd:element" mode="type">
        <xsl:if test="$omitComments">
            <xsl:comment>not known type</xsl:comment>
        </xsl:if>
        <xsl:element name="{concat($targetNSAlias,':',./@name)}" namespace="{$targetNS}"/>
    </xsl:template>

</xsl:stylesheet>