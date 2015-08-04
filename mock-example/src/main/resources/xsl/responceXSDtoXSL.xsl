<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:out="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService" >

    <xsl:import  href="XSDtoXSL.xsl"/>
    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:namespace-alias stylesheet-prefix="out" result-prefix="xsl"/>

    <!-- файл с темплейтом для soap header'а -->
    <xsl:include href="headerTemplates/headerTemplate.xsl"/>
    <!--вспомогательные функции-->
    <xsl:include href="xsltFunctions.xsl"/>

    <!--параметры, в которых указывается откуда и какой элемент брать как само тело сообщения-->
    <xsl:param name="operationsXSD" select="''"/> <!--FunFact - этот параметр выглядит как обяазтельный, и везде в остальных местах мы делаем вид, что он обязательный, но на самом деле он не обязателен. Если его не задать, то в переменную operationXsdSchema мы загрузим входной файл - то есть самое логичное место, где нужно искать нужный нам элемент -->
    <!--переменная с той схемой которую мы и превратим в xsl-->
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>

    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/>
    <!--выкидываем ошибку, если нам не дали имя тэга элемента-->
    <xsl:variable name="throwError" select="if ($rootElementName!='') then true() else error(QName('http://sbrf.ru/mockService', 'err01'),'rootElementName not defined')"/>

    <!-- Имя операции в заголвке -->
    <xsl:param name="operationName" select="$rootElementName"/>

    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="rootTypeName" select="mock:removeNamespaceAlias(/xsd:schema//xsd:element[@name=$rootElementName]/@type)"/>

    <xsl:param name="targetNS" select="$operationXsdSchema/@targetNamespace"/>
    <xsl:param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
    <!-- TODO выбрать этот параметр автоматом. Проблема в том, что задать его можно только захардкодив -->
    <xsl:param name="systemName" select="'CRM'"/>

    <xsl:variable name="dataNS" select="if ($targetNS!='') then mock:addDataToNamespaceUrl($targetNS, $rootElementName) else concat('http://sbrf.ru/mockService/',$rootElementName,'/Data/')"/>

    <!--В этой переменной идет выбор заголовка между разными системами. Сейчас выбор захорлкожен-->
    <!--!!! этот выбор захардкожен !!!-->
    <xsl:param name="headerType" select="if (contains($targetNS, 'bbmo')) then 'KD4' else 'NCP'"/>

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
    <!--TODO по идее импорты и инклюды сверху теряют одну большую возможность - инклюды и импорты внутри инклюдов и импортов. Пока в этом нет нужды - в текущих реализованахз xsd таких извращений нет. Но вообще надо бы срефакторить эти пременные в функцию с рекурсией. Так заодно можно будет убрать повторяющийся код из requestXSDtoXSL.xsl-->

    <!--Для упрощения кода нам нужны несколько списоков констант-->
    <!--типы элементов, которым нам интересны - в них может содержатся описание элементов-->
    <xsl:variable name="xsdTagsToImport" select="tokenize('element complexType',' ')"/>
    <!--имена атрибутов, которые нам интересны - в них может содержатся ссылки на другие элементы-->
    <xsl:variable name="atributesWithTypes" select="tokenize('ref base type',' ')"/>

    <!--Большой список типов - все типы что есть в исходном файле, инклюдах и импортах-->
    <xsl:variable name="typesList" select="$operationXsdSchema/*[local-name()=$xsdTagsToImport] | $includeFilesDocs/*[local-name()=$xsdTagsToImport] | $importFilesDocs/*[local-name()=$xsdTagsToImport]"/>

    <!--Имя дата-файла-->
    <!--TODO надо бы засунуть это в функцию. Вместе с аналогом из requestXSDtoXSL.xsl-->
    <xsl:param name="dataFileName" select="concat(replace(replace($rootElementName,'Rs',''),'Response',''),'Data.xml')"/>

    <!--Имя тэга, который будет использоваться как LinkedTag. Если не задать - возьмет первый-->
    <xsl:param name="tagNameToTakeLinkedTag" select="'*'"/>
    <!--xpath, по которому будет взят LinkedTag. Можно не переопределять, если определен $tagNameToTakeLinkedTag-->
    <xsl:param name="tagQuerryToTakeLinkedTag" select="if($tagNameToTakeLinkedTag='*') then '*[1]' else concat('//soap:Body/*//*[local-name()=''',$tagNameToTakeLinkedTag,'''][1]/text()')"/>

    <xsl:variable name="type" select="'response'"/>

    <!--переменная для отображение отладочных сообщений-->
    <xsl:variable name="DEBUG" select="true()"/>

    <xsl:template match="xsd:schema">
        <xsl:element name="xsl:stylesheet">
            <xsl:if test="$targetNS!=''">
                <xsl:namespace name="tns" select="$targetNS"/>
            </xsl:if>
            <xsl:namespace name="rsd" select="$dataNS"/>
            <xsl:if test="mock:SOAPNS($headerType)!=''">
                <xsl:namespace name="soap" select="mock:SOAPNS($headerType)"/>
            </xsl:if>
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
            <xsl:variable name="typesToTemplate" select="$baseContainer//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>

            <xsl:apply-templates select="$typesList[@name=mock:typesToImport($baseContainer)]" mode="template"/>
            <xsl:apply-templates select="$baseContainer" mode="base"/>
        </xsl:element>
    </xsl:template>

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
            <xsl:attribute name="select"><xsl:value-of select="$tagQuerryToTakeLinkedTag"/></xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">dataFileName</xsl:attribute>
            <xsl:attribute name="select">'../../data/<xsl:value-of select="$systemName"/>/xml/<xsl:value-of select="$dataFileName"/>'</xsl:attribute>
        </xsl:element>

        <xsl:call-template name="xslTeplateHeaderDeclaration">
            <xsl:with-param name="headerType" select="$headerType"/>
        </xsl:call-template>

        <xsl:text>&#xA;</xsl:text>
    </xsl:template>

    <xsl:template name="bodyDeclaration">
        <xsl:element name="xsl:template">
            <xsl:attribute name="match"><xsl:value-of select="mock:MainRootElement($headerType)"/></xsl:attribute><!--TODO исправить здесь; edit Забыл что именно нужно здесь исправить. Скорее всего как-то свзязано с неймспейсом soap. Но на эту строку уже заывязан автотест - по этой строке определяется, что xsl заглушка, а не драйвер. Поэтому без понимания на что менять лучше не менять -->
            <xsl:element name="xsl:variable">
                <xsl:attribute name="name">data</xsl:attribute>
                <xsl:attribute name="select">document($dataFileName)/rsd:data</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:variable">
                <xsl:attribute name="name">linkedTag</xsl:attribute>
                <xsl:attribute name="select">$name</xsl:attribute>
            </xsl:element>
            <xsl:call-template name="xslTeplateDeclaration">
                <xsl:with-param name="headerType" select="$headerType"/>
                <xsl:with-param name="operationName" select="$operationName"/>
                <xsl:with-param name="type" select="'response'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>