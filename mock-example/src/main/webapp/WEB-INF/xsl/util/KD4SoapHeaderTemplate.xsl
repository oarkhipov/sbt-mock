<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

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

</xsl:stylesheet>