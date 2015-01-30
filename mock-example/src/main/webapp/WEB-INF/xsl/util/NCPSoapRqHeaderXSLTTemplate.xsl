<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/">

    <!--заголовок из дата-файла-->
    <xsl:template name="NCPHeader" xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/1.03/Data/">
        <xsl:param name="response"/>
        <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
        <xsl:param name="operation-name"/>

        <!-- Optional params for optional header values -->
        <xsl:param name="id" select="null"/>
        <xsl:param name="correlation-id" select="null"/>
        <xsl:param name="eis-name" select="null"/>
        <xsl:param name="system-id" select="null"/>
        <xsl:param name="operation-version" select="null"/>
        <xsl:param name="user-id" select="null"/>
        <xsl:param name="user-name" select="null"/>
        <xsl:variable name="defaultId" select="'defaultId'"/>
        <soap-env:Header>
            <soap-env:message-id>
                <xsl:choose>
                    <xsl:when test="$id!='null'"><xsl:value-of select="$id"/></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:message-id"><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:message-id"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
                </xsl:choose>
            </soap-env:message-id>
            <soap-env:request-time><xsl:value-of select="$timestamp"/></soap-env:request-time>
            <soap-env:operation-name><xsl:value-of select="$operation-name"/></soap-env:operation-name>

            <xsl:choose>
                <xsl:when test="$correlation-id!='null'"><soap-env:correlation-id><xsl:value-of select="$correlation-id"/></soap-env:correlation-id></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:correlation-id"><soap-env:correlation-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:correlation-id"/></soap-env:correlation-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$eis-name!='null'"><soap-env:eis-name><xsl:value-of select="$eis-name"/></soap-env:eis-name></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:eis-name"><soap-env:eis-name><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:eis-name"/></soap-env:eis-name></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$system-id!='null'"><soap-env:system-id><xsl:value-of select="$system-id"/></soap-env:system-id></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:system-id"><soap-env:system-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:system-id"/></soap-env:system-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$operation-version!='null'"><soap-env:operation-version><xsl:value-of select="$operation-version"/></soap-env:operation-version></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:operation-version"><soap-env:operation-version><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:operation-version"/></soap-env:operation-version></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-id!='null'"><soap-env:user-id><xsl:value-of select="$user-id"/></soap-env:user-id></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"><soap-env:user-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"/></soap-env:user-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-name!='null'"><soap-env:user-name><xsl:value-of select="$user-name"/></soap-env:user-name></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"><soap-env:user-name><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"/></soap-env:user-name></xsl:when>
            </xsl:choose>
        </soap-env:Header>
    </xsl:template>

    <!--пример заголовка так, чтобы он выглядел как в настоязем сообщении-->
    <xsl:template name="NCPHeaderExample">
        <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
        <xsl:param name="operation-name"/>

        <!-- Optional params for optional header values -->
        <xsl:param name="id" select="null"/>
        <xsl:param name="correlation-id" select="null"/>
        <xsl:param name="eis-name" select="null"/>
        <xsl:param name="system-id" select="null"/>
        <xsl:param name="operation-version" select="null"/>
        <xsl:param name="user-id" select="null"/>
        <xsl:param name="user-name" select="null"/>
        <xsl:variable name="defaultId" select="'defaultId'"/>
        <soap-env:Header>
            <soap-env:message-id>
                <xsl:choose>
                    <xsl:when test="$id!='null'"><xsl:value-of select="$id"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
                </xsl:choose>
            </soap-env:message-id>
            <soap-env:request-time><xsl:value-of select="$timestamp"/></soap-env:request-time>
            <soap-env:operation-name><xsl:value-of select="$operation-name"/></soap-env:operation-name>

            <xsl:choose>
                <xsl:when test="$correlation-id!='null'"><soap-env:correlation-id><xsl:value-of select="$correlation-id"/></soap-env:correlation-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$eis-name!='null'"><soap-env:eis-name><xsl:value-of select="$eis-name"/></soap-env:eis-name></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$system-id!='null'"><soap-env:system-id><xsl:value-of select="$system-id"/></soap-env:system-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$operation-version!='null'"><soap-env:operation-version><xsl:value-of select="$operation-version"/></soap-env:operation-version></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-id!='null'"><soap-env:user-id><xsl:value-of select="$user-id"/></soap-env:user-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-name!='null'"><soap-env:user-name><xsl:value-of select="$user-name"/></soap-env:user-name></xsl:when>
            </xsl:choose>
        </soap-env:Header>
    </xsl:template>

    <!--часть XSD-схемы для вставки в Data-xsd-->
    <xsl:template name="NCPHeaderForXSD" xmlns:xsd="http://www.w3.org/2001/XMLSchema">

        <xsl:element name="xsd:import">
            <xsl:attribute name="namespace">http://sbrf.ru/commonTypes/dataTypes/</xsl:attribute>
            <xsl:attribute name="schemaLocation">../../../xsd/CRM/commonTypes/dataTypes.xsd</xsl:attribute>
        </xsl:element>

        <xsl:element name="xsd:import">
            <xsl:attribute name="namespace">http://sbrf.ru/commonTypes/simpleTypes/</xsl:attribute>
            <xsl:attribute name="schemaLocation">../../../xsd/CRM/commonTypes/simpleTypes.xsd</xsl:attribute>
        </xsl:element>

        <xsl:element name="xsd:complexType">
            <xsl:namespace name="st" select="'http://sbrf.ru/commonTypes/simpleTypes/'"/>
            <xsl:namespace name="dt" select="'http://sbrf.ru/commonTypes/dataTypes/'"/>
            <xsl:attribute name="name">Header</xsl:attribute>
            <xsl:element name="xsd:annotation">
                <xsl:element name="xsd:documentation">Заголовок сообщения</xsl:element>
            </xsl:element>
            <xsl:element name="xsd:sequence">
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">message-id</xsl:attribute>
                    <xsl:attribute name="type">xsd:string</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Уникальный идентификатор сообщения</xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">request-time</xsl:attribute>
                    <xsl:attribute name="type">dt:DateTime</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Системное время при отправке сообщения
                            АС-отправителем с учетом временной зоны</xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">correlation-id</xsl:attribute>
                    <xsl:attribute name="type">st:UUID</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Идентификатор корреляции</xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">eis-name</xsl:attribute>
                    <xsl:attribute name="type">st:SystemName_Type</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Идентификатор системы инициатора запроса/ответа
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">system-id</xsl:attribute>
                    <xsl:attribute name="type">st:SystemName_Type</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                    <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Идентификатор системы получателя запроса
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">operation-name</xsl:attribute>
                    <xsl:attribute name="type">st:OperationName_Type</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Название операции вызываемого сервиса
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">operation-version</xsl:attribute>
                    <xsl:attribute name="type">dt:DefString</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Версия операции</xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">user-id</xsl:attribute>
                    <xsl:attribute name="type">dt:DefString</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Идентификатор пользователя в системе инициаторе
                            запроса/ответа</xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">user-name</xsl:attribute>
                    <xsl:attribute name="type">dt:DefString</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>

                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Полное ФИО пользователя в системе инициаторе
                            запроса/ответа</xsl:element>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="xsd:any">
                    <xsl:attribute name="namespace">##other</xsl:attribute>
                    <xsl:attribute name="processContents">lax</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                    <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                    <xsl:element name="xsd:annotation">
                        <xsl:element name="xsd:documentation">Точка расширения заголовка (не используется,
                            добавлено на перспективу)</xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать список параметров, необходимых для заголовка-->
    <xsl:template name="NCPxslTeplateHeaderDeclaration"
                  xmlns:soap="http://sbrf.ru/NCP/esb/envelope/">
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">timestamp</xsl:attribute>
            <xsl:attribute name="select">string('2014-12-16T17:55:06.410+04:00')</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">id</xsl:attribute>
            <xsl:attribute name="select">null</xsl:attribute>
        </xsl:element>

        <xsl:comment>Optional params for optional header values</xsl:comment>
        <xsl:text>&#xA;</xsl:text>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">correlation-id</xsl:attribute>
            <xsl:attribute name="select">null</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">eis-name</xsl:attribute>
            <xsl:attribute name="select">null</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">system-id</xsl:attribute>
            <xsl:attribute name="select">null</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">operation-version</xsl:attribute>
            <xsl:attribute name="select">null</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">user-id</xsl:attribute>
            <xsl:attribute name="select">null</xsl:attribute>
        </xsl:element>
        <xsl:element name="xsl:param">
            <xsl:attribute name="name">user-name</xsl:attribute>
            <xsl:attribute name="select">null</xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--часть создания XSL - добавляет фрагмент кода, который будет делать необходимый заголовок-->
    <xsl:template name="NCPxslTeplateDeclaration"
                  xmlns:soap="http://sbrf.ru/NCP/esb/envelope/">
        <xsl:param name="operationName"/>

            <xsl:element name="xsl:element">
                <xsl:namespace name="soap" select="'http://sbrf.ru/NCP/esb/envelope/'"/>
                <xsl:attribute name="name">soap:Envelope</xsl:attribute>
                <xsl:element name="xsl:call-template">
                    <xsl:attribute name="name">NCPHeader</xsl:attribute>
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
                        <xsl:attribute name="name">timestamp</xsl:attribute>
                        <xsl:attribute name="select">$timestamp</xsl:attribute>
                    </xsl:element>
                    <xsl:element name="xsl:with-param">
                        <xsl:attribute name="name">id</xsl:attribute>
                        <xsl:attribute name="select">$id</xsl:attribute>
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