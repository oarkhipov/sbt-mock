<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:mock="http://sbrf.ru/mockService"><!--TODO заменить mock на namespace конфига -->
    <!-- xslt чтобы превратить xsd веб сервиса в data xsd - нашу xsd, которой должно соответствовать xml с данными.
     Представляет собой копипаст объявления с заменой неймспейсов-->
    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!--параметры, в которых указывается откуда и какой элемент брать как само тело сообщения-->
    <xsl:param name="operationsXSD" select="''"/>
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>

    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/><!-- Todo ошибка если не задано -->

    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="rootTypeName" select="mock:removeNamespaceAlias(/xsd:schema//xsd:element[@name=$rootElementName]/@type)"/>

    <!--Как будет называться в итоге элемент-->
    <xsl:param name="newDataNodeTypeName" select="if (string-length($rootTypeName)>0) then $rootTypeName else $rootElementName"/>

    <!-- TODO выбрать этот параметр автоматом -->
    <xsl:param name="systemName" select="'CRM'"/>

    <!-- неймспейс, в котором будут содрежатся наши данные. Получается припиской к стандратному неймспейсу субпути /Data/ -->
    <xsl:variable name="targetNS" select="mock:addDataToNamespaceUrl($operationXsdSchema/@targetNamespace)"/>

    <!-- неймспейс, которым обозначается tns в targetNamespace -->
    <xsl:variable name="tnsAlias" select="$operationXsdSchema/namespace::*[.=$operationXsdSchema/@targetNamespace and string-length(local-name(.))>0]/local-name()"/>

    <!--списски констант-->
    <!--тэги, содержащие типы-->
    <xsl:variable name="xsdTagsToImport" select="tokenize('element complexType',' ')"/>
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

    <!-- файл с темплейтом для soap header'а -->
    <xsl:include href="NCPSoapRqHeaderXSLTTemplate.xsl"/>
    <xsl:include href="xsltFunctions.xsl"/>

    <xsl:template match="xsd:schema">
        <xsl:comment>test <xsl:value-of select="$nsList"/></xsl:comment>
        <xsl:comment>test <xsl:value-of select="$nsAliasList"/></xsl:comment>
        <xsl:element name="xsd:schema">
            <xsl:namespace name="" select="$targetNS"/>
            <xsl:for-each select="$operationXsdSchema/namespace::*[.!=$operationXsdSchema/@targetNamespace and string-length(local-name(.))>0]">
                <xsl:namespace name="{local-name(.)}" select="."/>
            </xsl:for-each>
            <xsl:attribute name="targetNamespace">
                <xsl:value-of select="$targetNS"/>
            </xsl:attribute>
            <xsl:attribute name="elementFormDefault">qualified</xsl:attribute>
            <!--<xsl:for-each select="$operationXsdSchema/namespace::*[.!=$targetNS]">-->
                <!--<xsl:comment><xsl:value-of select="local-name(.)"/>=<xsl:value-of select="."/></xsl:comment>-->
            <!--</xsl:for-each>-->

            <xsl:apply-templates select="$operationXsdSchema/xsd:import" mode="imports"/>

            <!-- Заголовок из импортированного файла NCPSoapRqHeaderXSLTTemplate.xsl -->
            <xsl:call-template name="NCPHeaderForXSD"/>

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
                            <xsl:choose>
                                <!-- различаем запрос от ответа -->
                                <!--TODO вполне может быть такое, что запрос отличается каким-нибудь другим суффиком, или не имеет его вовсе. Нужно сделать параметр, которые если задатиь напрямую говорит что это запрос, а не ответ -->
                                <xsl:when test="contains($operationXsdSchema/@targetNamespace,'Rq')">
                                    <xsl:text>request</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>response</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:attribute name="type">
                            <xsl:value-of select="$newDataNodeTypeName"/>
                        </xsl:attribute>
                        <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                        <xsl:element name="xsd:annotation">
                            <xsl:element name="xsd:documentation">Ответ на запрос</xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>

            <!--<xsl:comment>test <xsl:value-of select="$rootTypeName"/></xsl:comment>-->
            <!--<xsl:comment>test <xsl:value-of select="$operationXsdSchema/xsd:complexType/@name"/></xsl:comment>-->
            <xsl:apply-templates select="$operationXsdSchema/xsd:complexType[@name=$rootTypeName] | $operationXsdSchema/xsd:element[@name=$rootElementName]/xsd:complexType" mode="copyMainType"/>

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

    <!-- перенос главного контейнера -->
    <xsl:template match="xsd:complexType" mode="copyMainType">
        <xsl:element name="xsd:complexType">
            <xsl:copy-of select="@*"/>
            <xsl:if test="not (@name) and parent::*[local-name()='element']/@name">
                <xsl:attribute name="name"><xsl:value-of select="parent::*/@name"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            <xsl:element name="xsd:attribute">
                <xsl:attribute name="name">name</xsl:attribute>
            </xsl:element>
        </xsl:element>

        <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
        <xsl:variable name="typesToImport" select=".//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$tnsAlias)[not(contains(.,':'))]"/>
        <!--<xsl:comment>test <xsl:value-of select="$typesToImport"/></xsl:comment>-->
        <!--<xsl:comment>test2 <xsl:value-of select="$typesList/local-name()"/></xsl:comment>-->

        <xsl:apply-templates select="$typesList[@name=$typesToImport]" mode="copyChildTypes"/>

    </xsl:template>

    <!-- перенос содержимого главного контейнера -->
    <xsl:template match="xsd:sequence" mode="copyMainTypeContainer">
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*"/>
            <xsl:element name="xsd:element">
                <xsl:attribute name="name">SoapHeader</xsl:attribute>
                <xsl:attribute name="type">Header</xsl:attribute>
                <xsl:attribute name="minOccurs">0</xsl:attribute>
            </xsl:element>
            <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@ref]" mode="copyMainTypeContainer">
        <xsl:variable name="refName" select="mock:removeNamespaceAlias(@ref, $tnsAlias)"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:if test="not(./@name)">
                <xsl:attribute name="name"><xsl:value-of select="$refName"/></xsl:attribute>
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
        <xsl:variable name="baseName" select="mock:removeNamespaceAlias(@base, $tnsAlias)"/>
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
        <xsl:variable name="typeName" select="mock:removeNamespaceAlias(@type, $tnsAlias)"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='type']"/>
            <xsl:attribute name="type"><xsl:value-of select="$typeName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="*" mode="copyMainTypeContainer">
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*"/>
            <xsl:apply-templates select="./*" mode="copyMainTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
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
        <xsl:variable name="typesToImport" select=".//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$tnsAlias)[not(contains(.,':'))]"/>
        <xsl:comment>test <xsl:value-of select="$typesToImport"/></xsl:comment>

        <xsl:apply-templates select="$typesList[@name=$typesToImport]" mode="copyChildTypes"/>
    </xsl:template>


    <xsl:template match="xsd:complexType" mode="copyChildTypes">
        <xsl:element name="xsd:complexType">
            <xsl:copy-of select="@*"/>
            <xsl:if test="not (@name) and parent::*[local-name()='element']/@name">
                <xsl:attribute name="name"><xsl:value-of select="parent::*/@name"/></xsl:attribute>
            </xsl:if>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
        </xsl:element>

        <!--получаем все тэги, которые могут содержать типы, которые нам нужны и берем только те, что лежат в нашем неймспейсе-->
        <xsl:variable name="typesToImport" select=".//@*[name()=$atributesWithTypes]/mock:removeNamespaceAlias(.,$tnsAlias)[not(contains(.,':'))]"/>
        <!--<xsl:comment>test <xsl:value-of select="$typesToImport"/></xsl:comment>-->

        <xsl:apply-templates select="$typesList[@name=$typesToImport]" mode="copyChildTypes"/>
    </xsl:template>

    <!--копирование содержимого типов-->

    <xsl:template match="xsd:element[@ref]" mode="copyTypeContainer">
        <xsl:variable name="refName" select="mock:removeNamespaceAlias(@ref, $tnsAlias)"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:if test="not(./@name)">
                <xsl:attribute name="name"><xsl:value-of select="$refName"/></xsl:attribute>
            </xsl:if>
            <xsl:copy-of select="./@*[local-name(.)!='ref']"/>
            <xsl:attribute name="type"><xsl:value-of select="$refName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@base]" mode="copyTypeContainer">
        <xsl:variable name="baseName" select="mock:removeNamespaceAlias(@base, $tnsAlias)"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='base']"/>
            <xsl:attribute name="base"><xsl:value-of select="$baseName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element[@type]" mode="copyTypeContainer">
        <xsl:variable name="typeName" select="mock:removeNamespaceAlias(@type, $tnsAlias)"/>
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*[local-name(.)!='type']"/>
            <xsl:attribute name="type"><xsl:value-of select="$typeName"/></xsl:attribute>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="*" mode="copyTypeContainer">
        <xsl:element name="xsd:{local-name(.)}">
            <xsl:copy-of select="./@*"/>
            <xsl:apply-templates select="./*" mode="copyTypeContainer"/>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="."/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>