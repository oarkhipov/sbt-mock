<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:out="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema" >

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:namespace-alias stylesheet-prefix="out" result-prefix="xsl"/>

    <xsl:param name="entryPointName" select="replace(xsd:schema/@targetNamespace,'^.+/(\w+)(/[0-9\.]+)?/$','$1')"/>
    <!--путь к верхней xsd с объявлением рут-элементов-->
    <xsl:param name="parrentXSDPath" select="'../../xsd/CRM/CRM.xsd'"/>
    <xsl:param name="rootXSD" select="document($parrentXSDPath)/xsd:schema"/>

    <xsl:param name="targetNS" select="xsd:schema/@targetNamespace"/>
    <xsl:param name="parrentNS" select="$rootXSD/@targetNamespace"/>
    <xsl:param name="systemName" select="'CRM'"/>
    <xsl:param name="soapEnvNS" select="'http://sbrf.ru/NCP/esb/envelope/'"/>
    <!--Имя тэга элемента. Скорее всего будет отличаться от $entryPointName, но брать его из другого файла-->
    <xsl:param name="rootElementName" select="$entryPointName"/>

    <xsl:template match="xsd:schema">
        <xsl:element name="xsl:stylesheet">
            <xsl:for-each select="namespace::*">
                <xsl:if test=". != 'http://www.w3.org/2001/XMLSchema'">
                    <xsl:namespace name="{local-name(.)}" select="."/>
                </xsl:if>
            </xsl:for-each>
            <xsl:namespace name="tns" select="$targetNS"/>
            <xsl:namespace name="rsd" select="concat($targetNS,'Data/')"/>
            <xsl:namespace name="soap" select="$soapEnvNS"/>
            <xsl:namespace name="{$systemName}" select="$parrentNS"/>
            <xsl:attribute name="version">1.0</xsl:attribute>
            <xsl:call-template name="headerDeclaration"/>
            <xsl:text>&#xA;</xsl:text>
            <xsl:call-template name="bodyDeclaration"/>
            <xsl:text>&#xA;&#xA;</xsl:text>
            <xsl:apply-templates select="./xsd:complexType[upper-case(@name)!=upper-case($entryPointName)]" mode="template"/>
            <xsl:apply-templates select="./xsd:complexType[upper-case(@name)=upper-case($entryPointName)]" mode="base"/>
        </xsl:element>
    </xsl:template>

    <!--Объявления заголовка - шапка, входные параметры-->
    <xsl:template name="headerDeclaration">
        <xsl:text>&#xA;</xsl:text>
        <xsl:element name="xsl:import">
            <xsl:attribute name="href">../util/NCPSoapRqHeaderXSLTTemplate.xsl</xsl:attribute>
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
            <xsl:attribute name="select">all</xsl:attribute>
        </xsl:element>
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
        <xsl:text>&#xA;</xsl:text>
    </xsl:template>


    <xsl:template name="bodyDeclaration">
        <xsl:element name="xsl:template">
            <xsl:attribute name="match">/</xsl:attribute>
            <xsl:element name="xsl:variable">
                <xsl:attribute name="name">data</xsl:attribute>
                <xsl:attribute name="select">//rsd:data</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:variable">
                <xsl:attribute name="name">linkedTag</xsl:attribute>
                <xsl:attribute name="select">$name</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:element">
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
                        <xsl:attribute name="select">string('<xsl:value-of select="$rootElementName"/>')</xsl:attribute>
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
                        <xsl:attribute name="name"><xsl:value-of select="$entryPointName"/></xsl:attribute>
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
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:complexType" mode="template">
        <xsl:param name="typeName" select="concat('tns:',self::*/@name)"/>
        <xsl:param name="tagName" select="//xsd:element[@type = $typeName]/@name"/>
        <xsl:param name="type" select="self::*"/>
        <xsl:for-each select="//xsd:element[@type = $typeName]/@name">
            <xsl:element name="xsl:template">
                <xsl:attribute name="match">rsd:<xsl:value-of select="."/></xsl:attribute>
                <xsl:element name="tns:{.}"  namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:apply-templates select="$type//xsd:element" mode="template"/>
                </xsl:element>
            </xsl:element>
            <xsl:text>&#xA;&#xA;</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="xsd:complexType" mode="base">
        <!--<xsl:comment>base test <xsl:value-of select="$entryPointName"/></xsl:comment>-->
        <xsl:element name="xsl:template">
            <xsl:attribute name="name"><xsl:value-of select="$entryPointName"/></xsl:attribute>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">response</xsl:attribute>
            </xsl:element>
            <xsl:element name="xsl:param">
                <xsl:attribute name="name">data</xsl:attribute>
            </xsl:element>
                <xsl:element name="xsl:element">
                    <xsl:attribute name="name"><xsl:value-of select="$systemName"/>:<xsl:value-of select="$rootElementName"/></xsl:attribute>
                    <xsl:apply-templates select=".//xsd:element" mode="base"/>
                </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:element" mode="base">
        <xsl:param name="typeName" select="replace(self::*/@type, 'tns:', '')"/>
        <!--<xsl:comment> <xsl:value-of select="$typeName"/> </xsl:comment>-->
        <xsl:choose>
            <xsl:when test="count(//*[name()='xsd:complexType'][upper-case(@name)=upper-case($typeName)])>0">
                <xsl:element name="xsl:apply-templates">
                    <xsl:attribute name="select">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
                </xsl:element>
            </xsl:when>
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                        <xsl:namespace name="tns" select="$targetNS"/>
                        <xsl:element name="xsl:value-of">
                            <xsl:attribute name="select">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:element name="xsl:value-of" >
                        <xsl:attribute name="select">$data/rsd:request[@name=$response]/rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    </xsl:element>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="xsd:element" mode="template">
        <xsl:param name="typeName" select="replace(self::*/@type, 'tns:', '')"/>
        <xsl:choose>
            <xsl:when test="count(//*[name()='xsd:complexType'][upper-case(@name)=upper-case($typeName)])>0">
            <!--<xsl:when test="count(//*[name()='xsd:complexType'][@name=replace(self::*/@type, 'tns:', '')])>0">-->
                <xsl:element name="xsl:apply-templates">
                    <xsl:attribute name="select">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
                </xsl:element>
            </xsl:when>
            <xsl:when test="@minOccurs=0">
                <xsl:element name="xsl:if">
                    <xsl:attribute name="test">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                        <xsl:namespace name="tns" select="$targetNS"/>
                        <xsl:element name="xsl:value-of">
                            <xsl:attribute name="select">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:element name="tns:{@name}" namespace="{$targetNS}">
                    <xsl:namespace name="tns" select="$targetNS"/>
                    <xsl:element name="xsl:value-of" >
                        <xsl:attribute name="select">./rsd:<xsl:value-of select="@name"/></xsl:attribute>
                    </xsl:element>
                </xsl:element>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>