<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:kd4="http://www.ibm.com/KD4Soa"
        xmlns:mq="http://sbrf.ru/prpc/mq/headers"
        xmlns:mock="http://sbrf.ru/mockService" >
    <xsl:import href="XSDToExampleXML.xsl"/>
    <xsl:import href="KD4SoapHeaderTemplate.xsl"/>

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <!--параметры, в которых указывается откуда и какой элемент брать как само тело сообщения-->
    <xsl:param name="operationsXSD" select="'../../xsd/CBBOL/BBMOOperationElements.xsd'"/>
    <xsl:variable name="operationXsdSchema" select="document($operationsXSD)/xsd:schema"/>

    <!--Имя тэга элемента-->
    <xsl:param name="rootElementName" select="''"/>
    <!--выкидываем ошибку, если нам не дали имя тэга элемента-->
    <xsl:variable name="throwError" select="if ($rootElementName!='') then true() else error(QName('http://sbrf.ru/mockService', 'err01'),'rootElementName not defined')"/>


    <!-- параметры заголовка -->
    <xsl:param name="kd4header" select="''"/>
    <xsl:param name="message-id" select="'message-id'"/>
    <xsl:param name="request-time" select="'2014-12-16T17:55:06.410'"/>
    <xsl:param name="correlation-id" select="''"/>
    <xsl:param name="eis-name" select="''"/>
    <xsl:param name="system-id" select="''"/>
    <!--задано ниже в зависимости от тэга <xsl:param name="operation-name" select="'operation-name'"/>-->
    <xsl:param name="operation-version" select="''"/>
    <xsl:param name="user-id" select="''"/>
    <xsl:param name="user-name" select="''"/>
    <xsl:param name="proc-inst-tb" select="''"/>

    <!--*********************************************-->
    <!--параметры для ипротированного XSDToExampleXML-->
    <!--*********************************************-->
    <!-- Этот параметр нужен когда имя главного элемента запроса не соответвует тому что мы взяли из неймспейса. Тогда его можно указать параметром -->
    <xsl:param name="rootTypeName" select="mock:removeNamespaceAlias(/xsd:schema//xsd:element[@name=$rootElementName]/@type)"/>

    <!--система-->
    <xsl:param name="systemName" select="'BBMO'"/>

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
    <xsl:variable name="xsdNsAlias" select="local-name($operationXsdSchema/namespace::*[.='http://www.w3.org/2001/XMLSchema'])"/>
    <!--нэймспейс-->
    <xsl:variable name="targetNS" select="$operationXsdSchema/@targetNamespace"/>
    <!--алиас неймспейса. Лучше не менрять-->
    <xsl:param name="targetNSAlias" select="'tns'"/>
    <!--алиас неймспейса, который используется в исходной xsd-->
    <xsl:variable name="localTargetNSAlias" select="local-name($operationXsdSchema/namespace::*[.=$targetNS][string-length(local-name(.))>0])"/>
    <!--имя операции-->
    <xsl:param name="operationName" select="$rootElementName"/>

    <!-- инклюды схем -->
    <xsl:variable name="includeFilesList" select="$operationXsdSchema/xsd:include/@schemaLocation"/>
    <xsl:variable name="includeFilesDocs" select="document($includeFilesList)/xsd:schema"/>

    <!-- импорт схем -->
    <xsl:variable name="importFilesList" select="$operationXsdSchema/xsd:import/@schemaLocation"/>
    <xsl:variable name="importFilesNs" select="$operationXsdSchema/xsd:import/@namespace"/>
    <xsl:variable name="importFilesNsAlias" select="$operationXsdSchema/namespace::*[.=$importFilesNs]/local-name()"/>
    <xsl:variable name="importFilesDocs" select="document($importFilesList)/xsd:schema"/>

    <!--список всех типов, которые объявленны в схеме-->
    <xsl:variable name="typesList" select="$operationXsdSchema//(xsd:complexType | xsd:simpleType)/@name | $importFilesDocs/(xsd:complexType | xsd:simpleType)/@name | $includeFilesDocs/(xsd:complexType | xsd:simpleType)/@name"/>


    <xsl:template match="xsd:schema">
        <xsl:element name="soap:Envelope">
            <xsl:call-template name="KD4SoapHeader">
                <xsl:with-param name="kd4header" select="$kd4header"/>
                <xsl:with-param name="message-id" select="$message-id"/>
                <xsl:with-param name="request-time" select="$request-time"/>
                <xsl:with-param name="correlation-id" select="$correlation-id"/>
                <xsl:with-param name="eis-name" select="$eis-name"/>
                <xsl:with-param name="system-id" select="$system-id"/>
                <xsl:with-param name="operation-name" select="$operationName"/>
                <xsl:with-param name="operation-version" select="$operation-version"/>
                <xsl:with-param name="user-id" select="$user-id"/>
                <xsl:with-param name="user-name" select="$user-name"/>
                <xsl:with-param name="proc-inst-tb" select="$proc-inst-tb"/>
            </xsl:call-template>
            <xsl:call-template name="KD4SoapBody"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="KD4SoapBody">
        <xsl:element name="soap:Body">
            <xsl:call-template name="operationBody"/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="operationBody">
        <xsl:choose>
            <xsl:when test="$operationXsdSchema/xsd:element[@name=$rootElementName]">
                <xsl:apply-templates select="$operationXsdSchema/xsd:element[@name=$rootElementName]" mode="rootBodyElement">
                    <xsl:with-param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
                </xsl:apply-templates>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates select="$operationXsdSchema/xsd:complexType[@name=$rootTypeName]" mode="rootBodyElement">
                    <xsl:with-param name="parrentNS" select="/xsd:schema/@targetNamespace"/>
                </xsl:apply-templates>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>