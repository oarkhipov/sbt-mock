<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="KD4Header" xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/1.03/Data/"
                  xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:kd4="http://www.ibm.com/KD4Soa"
                  xmlns:mq="http://sbrf.ru/prpc/mq/headers">
        <xsl:param name="response"/>
        <xsl:param name="request-time" select="'2014-12-16T17:55:06.410+04:00'"/>
        <xsl:param name="operation-name"/>
        <xsl:param name="message-id" select="'message-id'"/>

        <!-- Optional params for optional header values -->
        <xsl:param name="kd4header" select="''"/>
        <xsl:param name="correlation-id" select="''"/>
        <xsl:param name="eis-name" select="''"/>
        <xsl:param name="system-id" select="''"/>
        <xsl:param name="operation-version" select="''"/>
        <xsl:param name="user-id" select="''"/>
        <xsl:param name="user-name" select="''"/>
        <xsl:param name="proc-inst-tb" select="''"/>
        <xsl:variable name="defaultId" select="'defaultId'"/>

        <soap:Header>
            <xsl:if test="$kd4header!=''">
                <kd4:KD4SoapHeaderV2><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:KD4SoapHeaderV2"/></kd4:KD4SoapHeaderV2>
            </xsl:if>
            <mq:AsyncHeader>
                <soap:message-id>
                    <xsl:choose>
                        <xsl:when test="$message-id!=''"><xsl:value-of select="$message-id"/></xsl:when>
                        <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:message-id"><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:message-id"/></xsl:when>
                        <xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
                    </xsl:choose>
                </soap:message-id>
                <soap:request-time><xsl:value-of select="$request-time"/></soap:request-time>
                <xsl:choose>
                    <xsl:when test="$correlation-id!=''"><soap:correlation-id><xsl:value-of select="$correlation-id"/></soap:correlation-id></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:correlation-id"><soap:correlation-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:correlation-id"/></soap:correlation-id></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$eis-name!=''"><soap:eis-name><xsl:value-of select="$eis-name"/></soap:eis-name></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:eis-name"><soap:eis-name><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:eis-name"/></soap:eis-name></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$system-id!=''"><soap:system-id><xsl:value-of select="$system-id"/></soap:system-id></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:system-id"><soap:system-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:system-id"/></soap:system-id></xsl:when>
                </xsl:choose>
                <soap:operation-name><xsl:value-of select="$operation-name"/></soap:operation-name>
                <xsl:choose>
                    <xsl:when test="$operation-version!=''"><soap:operation-version><xsl:value-of select="$operation-version"/></soap:operation-version></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:operation-version"><soap:operation-version><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:operation-version"/></soap:operation-version></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$user-id!=''"><soap:user-id><xsl:value-of select="$user-id"/></soap:user-id></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:user-id"><soap:user-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:user-id"/></soap:user-id></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$user-name!=''"><soap:user-name><xsl:value-of select="$user-name"/></soap:user-name></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:user-id"><soap:user-name><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:user-id"/></soap:user-name></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$proc-inst-tb!=''"><soap:proc-inst-tb><xsl:value-of select="$proc-inst-tb"/></soap:proc-inst-tb></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:proc-inst-tb"><soap:proc-inst-tb><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:AsyncHeader/rsd:proc-inst-tb"/></soap:proc-inst-tb></xsl:when>
                </xsl:choose>
            </mq:AsyncHeader>
        </soap:Header>
    </xsl:template>


    <xsl:template name="KD4SoapHeader"
                  xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:kd4="http://www.ibm.com/KD4Soa"
                  xmlns:mq="http://sbrf.ru/prpc/mq/headers">
        <xsl:param name="kd4header" select="'kd4Header'"/>
        <xsl:param name="message-id" select="'message-id'"/>
        <xsl:param name="request-time" select="'2014-12-16T17:55:06.410+04:00'"/>
        <xsl:param name="correlation-id" select="''"/>
        <xsl:param name="eis-name" select="''"/>
        <xsl:param name="system-id" select="''"/>
        <xsl:param name="operation-name" select="'operation-name'"/>
        <xsl:param name="operation-version" select="''"/>
        <xsl:param name="user-id" select="''"/>
        <xsl:param name="user-name" select="''"/>
        <xsl:param name="proc-inst-tb" select="''"/>
        <xsl:element name="soap:Header">
            <xsl:element name="kd4:KD4SoapHeaderV2"><xsl:value-of select="$kd4header"/></xsl:element>
            <xsl:element name="mq:AsyncHeader">
                <xsl:element name="mq:message-id"><xsl:value-of select="$message-id"/></xsl:element>
                <xsl:element name="mq:request-time"><xsl:value-of select="$request-time"/></xsl:element>
                <xsl:if test="$correlation-id!=''">
                    <xsl:element name="mq:correlation-id"><xsl:value-of select="$correlation-id"/></xsl:element>
                </xsl:if>
                <xsl:if test="$eis-name!=''">
                    <xsl:element name="mq:eis-name"><xsl:value-of select="$eis-name"/></xsl:element>
                </xsl:if>
                <xsl:if test="$system-id!=''">
                    <xsl:element name="mq:system-id"><xsl:value-of select="$system-id"/></xsl:element>
                </xsl:if>
                <xsl:element name="mq:operation-name"><xsl:value-of select="$operation-name"/></xsl:element>
                <xsl:if test="$operation-version!=''">
                    <xsl:element name="mq:operation-version"><xsl:value-of select="$operation-version"/></xsl:element>
                </xsl:if>
                <xsl:if test="$user-id!=''">
                    <xsl:element name="mq:user-id"><xsl:value-of select="$user-id"/></xsl:element>
                </xsl:if>
                <xsl:if test="$user-name!=''">
                    <xsl:element name="mq:user-name"><xsl:value-of select="$user-name"/></xsl:element>
                </xsl:if>
                <xsl:if test="$proc-inst-tb!=''">
                    <xsl:element name="mq:proc-inst-tb"><xsl:value-of select="$proc-inst-tb"/></xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="KD4HeaderForXSD" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
        <xsl:element name="xsd:complexType">
            <xsl:namespace name="dt" select="'http://sbrf.ru/prpc/bbmo/dataTypes/10'"/>
            <xsl:namespace name="st" select="'http://sbrf.ru/prpc/bbmo/simpleTypes/10'"/>
            <xsl:attribute name="name">Header</xsl:attribute>
            <xsl:element name="xsd:annotation">
                <xsl:element name="xsd:documentation">Заголовок сообщения</xsl:element>
            </xsl:element>
                <xsd:sequence>
                    <xsd:element name="KD4SoapHeaderV2" type="dt:String">
                        <xsd:annotation>
                            <xsd:documentation>Заголовок SOAP-сообщения</xsd:documentation>
                        </xsd:annotation>
                    </xsd:element>
                    <xsd:element name="AsyncHeader">
                        <xsd:annotation>
                            <xsd:documentation>Заголовок SOAP-сообщения</xsd:documentation>
                        </xsd:annotation>
                        <xsd:complexType>
                            <xsd:sequence>
                                <xsd:element name="message-id" type="st:UUID">
                                    <xsd:annotation>
                                        <xsd:documentation>Уникальный идентификатор сообщения, передаваемого в рамках SOAP-пакета</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="request-time" type="dt:DateTime">
                                    <xsd:annotation>
                                        <xsd:documentation>Системное время при отправке сообщения АС-отправителем с учетом временной зоны</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="correlation-id" type="st:UUID" minOccurs="0">
                                    <xsd:annotation>
                                        <xsd:documentation>Идентификатор корреляции</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="eis-name" type="st:SystemName_Type" minOccurs="0">
                                    <xsd:annotation>
                                        <xsd:documentation>Идентификатор системы инициатора запроса/ответа</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="system-id" type="st:SystemName_Type" minOccurs="0" maxOccurs="unbounded">
                                    <xsd:annotation>
                                        <xsd:documentation>Идентификатор системы получателя запроса</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="operation-name" type="st:OperationName_Type">
                                    <xsd:annotation>
                                        <xsd:documentation>Название операции вызываемого сервиса</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="operation-version" type="dt:DefString" minOccurs="0">
                                    <xsd:annotation>
                                        <xsd:documentation>Версия операции</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="user-id" type="dt:DefString" minOccurs="0">
                                    <xsd:annotation>
                                        <xsd:documentation>Идентификатор пользователя в системе инициаторе запроса/ответа</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="user-name" type="dt:DefString" minOccurs="0">
                                    <xsd:annotation>
                                        <xsd:documentation>Полное ФИО пользователя в системе инициаторе запроса/ответа</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:element name="proc-inst-tb" type="st:TB_Type" minOccurs="0">
                                    <xsd:annotation>
                                        <xsd:documentation>Код ТБ</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:element>
                                <xsd:any namespace="##other" processContents="lax" minOccurs="0" maxOccurs="unbounded">
                                    <xsd:annotation>
                                        <xsd:documentation>Точка расширения заголовка (не используется, добавлено на персективу)</xsd:documentation>
                                    </xsd:annotation>
                                </xsd:any>
                            </xsd:sequence>
                        </xsd:complexType>
                    </xsd:element>
                </xsd:sequence>
        </xsl:element>
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать список параметров, необходимых для заголовка-->
    <xsl:template name="KD4xslTeplateHeaderDeclaration">
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">request-time</xsl:attribute>
            <xsl:attribute name="select">string('2014-12-16T17:55:06.410+04:00')</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">kd4header</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">message-id</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>

        <xsl:comment>Optional params for optional header values</xsl:comment>
        <xsl:text>&#xA;</xsl:text>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">correlation-id</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">eis-name</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">system-id</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">operation-version</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">user-id</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">user-name</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">proc-inst-tb</xsl:attribute>
            <xsl:attribute name="select">''</xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать необходимый заголовок-->
    <xsl:template name="KD4xslTeplateDeclaration"
                  xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
        <xsl:param name="operationName"/>
            <xsl:element name="xsl:element">
                <xsl:namespace name="soap" select="'http://schemas.xmlsoap.org/soap/envelope/'"/>
                <xsl:attribute name="name">soap:Envelope</xsl:attribute>
                <xsl:element name="xsl:call-template">
                    <xsl:attribute name="name">KD4Header</xsl:attribute>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">response</xsl:attribute>
                        <xsl:element name="xsl:choose">
                            <xsl:element name="xsl:when">
                                <xsl:attribute name="test">count(./rsd:request[@name=$linkedTag])=1</xsl:attribute>
                                <xsl:element name="xsl:value-of">
                                    <xsl:attribute name="select">$linkedTag</xsl:attribute>
                                </xsl:element>
                            </xsl:element>
                            <xsl:element name="xsl:otherwise">default</xsl:element>
                        </xsl:element>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">request-time</xsl:attribute>
                        <xsl:attribute name="select">$request-time</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">message-id</xsl:attribute>
                        <xsl:attribute name="select">$message-id</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">operation-name</xsl:attribute>
                        <xsl:attribute name="select">string('<xsl:value-of select="$operationName"/>')</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">correlation-id</xsl:attribute>
                        <xsl:attribute name="select">$correlation-id</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">eis-name</xsl:attribute>
                        <xsl:attribute name="select">$eis-name</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">system-id</xsl:attribute>
                        <xsl:attribute name="select">$system-id</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">operation-version</xsl:attribute>
                        <xsl:attribute name="select">$operation-version</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">user-id</xsl:attribute>
                        <xsl:attribute name="select">$user-id</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">user-name</xsl:attribute>
                        <xsl:attribute name="select">$user-name</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">kd4header</xsl:attribute>
                        <xsl:attribute name="select">$kd4header</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">proc-inst-tb</xsl:attribute>
                        <xsl:attribute name="select">$proc-inst-tb</xsl:attribute>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="soap:Body">
                    <xsl:element name="xsl:call-template">
                        <xsl:attribute name="name"><xsl:value-of select="$operationName"/></xsl:attribute>
                        <xsl:element name="xsl:with-param">
                            <xsl:attribute name="name">data</xsl:attribute>
                            <xsl:attribute name="select">$data</xsl:attribute>
                        </xsl:element>
                        <xsl:element name="xsl:with-param">
                            <xsl:attribute name="name">response</xsl:attribute>
                            <xsl:element name="xsl:choose">
                                <xsl:element name="xsl:when">
                                    <xsl:attribute name="test">count($data/rsd:request[@name=$linkedTag])=1</xsl:attribute>
                                    <xsl:element name="xsl:value-of">
                                        <xsl:attribute name="select">$linkedTag</xsl:attribute>
                                    </xsl:element>
                                </xsl:element>
                                <xsl:element name="xsl:otherwise">default</xsl:element>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
    </xsl:template>


</xsl:stylesheet>