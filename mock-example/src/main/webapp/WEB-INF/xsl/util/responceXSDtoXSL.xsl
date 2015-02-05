<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:out="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService" >

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:namespace-alias stylesheet-prefix="out" result-prefix="xsl"/>

    <!-- файл с темплейтом для soap header'а -->
    <xsl:include href="headerTemplate.xsl"/>
    <!--вспомогательные функции-->
    <xsl:include href="xsltFunctions.xsl"/>

    <!--параметры, в которых указывается откуда и какой элемент брать как само тело сообщения-->
    <xsl:param name="operationsXSD" select="''"/> <!--FunFact - этот параметр выглядит как обяазтельный, и везде в остальных местах мы делаем вид, что он обязательный, но на самом деле он не обязателен. Если его не задать, то в переменную operationXsdSchema мы загрузим входной файл - то есть самое логичное место, где нужно искать нужный нам элемент -->
    <!--переменная с той схемой которую мы и превратим в xsl-->
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>

    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/>
    <!--выкидываем ошибку, если нам не дали имя тэга элемента-->
    <xsl:variable name="throwError" select="if ($rootElementName!='') then true() else error(QName('http://sbrf.ru/mockService', 'err01'),'rootElementName not defined')"/><!--TODO заменить mock на namespace конфига -->

    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="rootTypeName" select="mock:removeNamespaceAlias(/xsd:schema//xsd:element[@name=$rootElementName]/@type)"/>

    <xsl:param name="targetNS" select="$operationXsdSchema/@targetNamespace"/>
    <xsl:param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
    <!-- TODO выбрать этот параметр автоматом. Проблема в том, что задать его можно только захардкодив -->
    <xsl:param name="systemName" select="'CRM'"/>

    <!--Этим параметром можно выбрать алиас, под которым будет отображаться target namespace. Лучше не менять. Сделанно для того, чтобы не делать алиаса когда он пустой-->
    <xsl:param name="tNSAlias" select="if ($targetNS!='') then 'tns' else ''"/>
    <xsl:variable name="tns_" select="if ($tNSAlias!='') then concat($tNSAlias, ':') else ''"/>
    <xsl:variable name="dataNS" select="if ($targetNS!='') then mock:addDataToNamespaceUrl($targetNS) else concat('http://sbrf.ru/mockService/',$rootElementName,'/Data/')"/> <!--TODO заменить mock на namespace конфига -->

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
    <xsl:param name="tagQuerryToTakeLinkedTag" select="if($tagNameToTakeLinkedTag='*') then '*[1]' else concat('*[local-name()=''',$tagNameToTakeLinkedTag,''']')"/>

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
        <xsl:variable name="importOnThislevel" select="$baseElement//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>
        <xsl:for-each select="$importOnThislevel"><xsl:value-of select="."/></xsl:for-each>
    </xsl:function>

    <xsl:template match="xsd:schema">
        <xsl:element name="xsl:stylesheet">
            <!--TODO поставить if условия на неймспейсы, как в requestXSDtoXSL.xsl-->
            <xsl:namespace name="tns" select="$targetNS"/>
            <xsl:namespace name="rsd" select="concat($targetNS,'Data/')"/>
            <xsl:namespace name="soap" select="mock:SOAPNS($headerType)"/>
            <xsl:namespace name="{$systemName}" select="$parrentNS"/>
            <xsl:attribute name="version">1.0</xsl:attribute>
            <xsl:call-template name="headerDeclaration"/>
            <xsl:text>&#xA;</xsl:text>
            <xsl:call-template name="bodyDeclaration"/>
            <xsl:text>&#xA;&#xA;</xsl:text>

            <xsl:variable name="baseContainer" select="if (count($typesList[./local-name()='complexType' and @name=$rootTypeName])=1) then $typesList[./local-name()='complexType' and @name=$rootTypeName] else $typesList[@name=$rootElementName]"/>

            <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
            <xsl:variable name="typesToTemplate" select="$baseContainer//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$localTargetNSAlias)[not(contains(.,':'))]"/>

            <xsl:apply-templates select="$typesList[@name=mock:typesToImport($baseContainer)]" mode="template"/>
            <xsl:apply-templates select="$baseContainer" mode="base"/>
        </xsl:element>
    </xsl:template>

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
            <xsl:attribute name="select">//soap:Body/*//<xsl:value-of select="$tagQuerryToTakeLinkedTag"/>[1]/text()</xsl:attribute>
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
            <xsl:attribute name="match">soap:Envelope</xsl:attribute><!--TODO исправить здесь -->
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
                <xsl:with-param name="operationName" select="$rootElementName"/>
                <xsl:with-param name="type" select="'response'"/>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:complexType" mode="template" priority="2">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>
        <xsl:param name="type" select="self::*"/>
        <!--<xsl:comment>test <xsl:value-of select="$typesList/@name"/></xsl:comment>-->
        <xsl:for-each select="$typesList//xsd:element[mock:removeNamespaceAlias(@type, $localTargetNSAlias) = $typeName]/@name">
            <xsl:element name="xsl:template">
                <xsl:attribute name="match">rsd:<xsl:value-of select="."/></xsl:attribute>
                <xsl:element name="tns:{.}"  namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:apply-templates select="$type//xsd:element" mode="Inside">
                        <xsl:with-param name="dataPath" select="'./rsd:'"/>
                    </xsl:apply-templates>
                </xsl:element>
            </xsl:element>
            <xsl:text>&#xA;&#xA;</xsl:text>
        </xsl:for-each>

    </xsl:template>

    <xsl:template match="xsd:element" mode="template" priority="1">
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@name, $localTargetNSAlias)"/>
        <xsl:element name="xsl:template">
            <xsl:attribute name="match">rsd:<xsl:value-of select="$typeName"/></xsl:attribute>
            <xsl:element name="tns:{$typeName}"  namespace="{$targetNS}">
                <xsl:namespace name="tns" select="$targetNS"/>
                <xsl:apply-templates select="./xsd:complexType/xsd:sequence/xsd:element" mode="Inside">
                    <xsl:with-param name="dataPath" select="'./rsd:'"/>
                </xsl:apply-templates>
            </xsl:element>
        </xsl:element>
        <xsl:text>&#xA;&#xA;</xsl:text>

    </xsl:template>

    <xsl:template match="*[local-name()=$xsdTagsToImport]" mode="base">
        <xsl:variable name="mainElementNSAlias" select="if ($targetNS=$parrentNS) then 'tns' else $systemName"/>
        <!--<xsl:comment><xsl:value-of select="concat($targetNS,'-',$parrentNS)"/></xsl:comment>-->
        <xsl:element name="xsl:template">
            <xsl:attribute name="name"><xsl:value-of select="$rootElementName"/></xsl:attribute>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">response</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">data</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:element">
                <xsl:attribute name="name"><xsl:value-of select="$mainElementNSAlias"/>:<xsl:value-of select="$rootElementName"/></xsl:attribute>
                <xsl:if test="./xsd:complexContent/xsd:extension">
                    <xsl:variable name="baseLocalName" select="mock:removeNamespaceAlias(./xsd:complexContent/xsd:extension/@base)"/>
                    <xsl:variable name="baseType" select="$typesList[@name=$baseLocalName]"/>
                    <xsl:apply-templates select="$baseType/xsd:sequence
                                                | $baseType/xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
                                                | $baseType/xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element))" mode="Inside">
                        <xsl:with-param name="dataPath" select="'$data/rsd:response[@name=$response]/rsd:'"/>
                    </xsl:apply-templates>
                </xsl:if>
                <xsl:apply-templates select="./xsd:sequence
                                                | ./xsd:complexContent/xsd:extension/xsd:sequence/xsd:element
                                                | ./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element))" mode="Inside">

                    <xsl:with-param name="dataPath" select="'$data/rsd:response[@name=$response]/rsd:'"/>
                </xsl:apply-templates>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@name]" mode="Inside" priority="1">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:choose>
            <!--TODO рассмоьтреть случай, когда есть @maxOccurs. тогда надо длеать еще темплейт -->
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                        <xsl:namespace name="tns" select="$targetNS"/>
                        <xsl:element name="xsl:value-of">
                            <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:element name="xsl:value-of" >
                        <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
                    </xsl:element>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="xsd:element[./xsd:complexType/xsd:sequence]" mode="Inside" priority="2">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:variable name="elementName" select="@name"/>
        <xsl:choose>
            <!--TODO рассмотреть случай, когда есть @maxOccurs. тогда надо длеать еще темплейт -->
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test"><xsl:value-of select="$dataPath"/><xsl:value-of select="$elementName"/></xsl:attribute>
                    <xsl:element name="tns:{$elementName}" namespace="{$targetNS}">
                        <xsl:namespace name="tns" select="$targetNS"/>
                        <xsl:apply-templates select="./xsd:complexType/(xsd:sequence/xsd:element
                                                        | xsd:complexContent/(xsd:sequence/xsd:element
                                                                            | xsd:extension/xsd:sequence/xsd:element))" mode="Inside">
                            <xsl:with-param name="dataPath" select="replace($dataPath, '^(.+?)/rsd:$', concat('$1/rsd:',$elementName,'/rsd:'))"/><!-- добавляем в конец пути этот элемент и ищем внутри -->
                        </xsl:apply-templates>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="tns:{$elementName}" namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <!--<xsl:element name="xsl:value-of" >-->
                    <!--<xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="$elementName"/></xsl:attribute>-->
                    <!--</xsl:element>-->
                    <xsl:apply-templates select="./xsd:complexType/xsd:sequence/xsd:element" mode="Inside">
                        <xsl:with-param name="dataPath" select="replace($dataPath, '^(.+?)/rsd:$', concat('$1/rsd:',$elementName,'/rsd:'))"/><!-- добавляем в конец пути этот элемент и ищем внутри -->
                    </xsl:apply-templates>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="xsd:element[@name and mock:removeNamespaceAlias(./@type, $localTargetNSAlias)=$typesList/@name]" mode="Inside"  priority="3">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:param name="typeName" select="mock:removeNamespaceAlias(./@type, $localTargetNSAlias)"/>
        <!--<xsl:comment> <xsl:value-of select="$typeName"/> </xsl:comment>-->
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="@name"/></xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@ref]" mode="Inside"  priority="4">
        <xsl:param name="dataPath"/> <!-- в данном параметре харниться путь из дата-xml, по которому будет получаться значение элемента -->
        <xsl:param name="refName" select="mock:removeNamespaceAlias(./@ref, $localTargetNSAlias)"/>
        <!--<xsl:comment>ref <xsl:value-of select="''"/> </xsl:comment>-->
        <xsl:element name="xsl:apply-templates">
            <xsl:attribute name="select"><xsl:value-of select="$dataPath"/><xsl:value-of select="$refName"/></xsl:attribute>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>