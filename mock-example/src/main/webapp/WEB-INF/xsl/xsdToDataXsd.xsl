<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:dt="http://sbrf.ru/commonTypes/dataTypes/"
                xmlns:st="http://sbrf.ru/commonTypes/simpleTypes/">
    <xsl:import href="NCPSoapRqHeaderXSLTTemplate.xsl"/>

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <xsl:param name="entryPointName"/>

    <xsl:template match="xsd:schema">
        <xsl:variable name="targetNS" select="concat(./@targetNamespace,'Data/')"/>
        <!--<xsl:element name="xsd:schema">-->
        <xsl:element name="xsd:schema">
            <xsl:namespace name="" select="$targetNS"/>
            <xsl:namespace name="st" select="'http://sbrf.ru/commonTypes/simpleTypes/'"/>
            <xsl:namespace name="dt" select="'http://sbrf.ru/commonTypes/dataTypes/'"/>
            <xsl:namespace name="crmct" select="'http://sbrf.ru/NCP/CRM/CommonTypes/'"/>
            <xsl:attribute name="targetNamespace">
                <xsl:value-of select="$targetNS"/>
            </xsl:attribute>
            <xsl:attribute name="elementFormDefault">qualified</xsl:attribute>


            <xsl:apply-templates select="./xsd:import"/>

            <xsl:element name="xsd:element">
                <xsl:attribute name="name">data</xsl:attribute>
                <xsl:attribute name="type">dataValue</xsl:attribute>
            </xsl:element>

            <xsl:element name="xsd:complexType">
                <xsl:attribute name="name">dataValue</xsl:attribute>
                <xsl:element name="xsd:sequence">
                    <xsl:element name="xsd:element">
                        <xsl:attribute name="name">
                            <xsl:choose>
                                <xsl:when test="contains(./@targetNamespace,'Rq')">
                                    <xsl:text>request</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>response</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                        <xsl:attribute name="type">
                            <xsl:value-of select="$entryPointName"/>
                             <!--Более крутой способ только для XPATH 2.0 <xsl:value-of select="replace(./@targetNamespace,'^.+/(\w+)(/[0-9\.]+)?/$','$1')"/>-->
                        </xsl:attribute>
                        <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                        <xsl:element name="xsd:annotation">
                            <xsl:element name="xsd:documentation">Ответ на запрос</xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:element>

            <xsl:call-template name="NCPHeaderForXSD"/>

            <xsl:apply-templates select="./xsd:complexType[@name=$entryPointName]">
                <xsl:with-param name="entryPointName" select="$entryPointName"/>
            </xsl:apply-templates>

        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:import">
        <xsl:element name="xsd:import">
            <xsl:attribute name="namespace">
                <xsl:value-of select="./@namespace"/>
            </xsl:attribute>
            <xsl:attribute name="schemaLocation">
                <xsl:text>../../../xsd/CRM/</xsl:text><xsl:value-of select="./@schemaLocation"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <xsl:template match="xsd:complexType">
        <xsl:param name="entryPointName"/>
        <xsl:copy copy-namespaces="no" >
            <xsl:copy-of select="@*"/>
            <xsl:choose>
                <xsl:when test="./@name=$entryPointName">
                    <xsl:for-each select="*">
                        <xsl:apply-templates select=".">
                            <xsl:with-param name="entryPointName" select="$entryPointName"/>
                        </xsl:apply-templates>
                    </xsl:for-each>
                    <xsl:element name="xsd:attribute">
                        <xsl:attribute name="name">name</xsl:attribute>
                    </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:for-each select="*"><xsl:copy-of select="." copy-namespaces="no"/></xsl:for-each>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*[name(parent::*)='xsd:complexType']">
        <xsl:param name="entryPointName"/>
        <xsl:copy copy-namespaces="no">
            <xsl:if test="name(.)='xsd:sequence'">
                <xsl:element name="xsd:element">
                    <xsl:attribute name="name">SoapHeader</xsl:attribute>
                    <xsl:attribute name="type">Header</xsl:attribute>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                </xsl:element>
            </xsl:if>
            <xsl:for-each select="*">
                <xsl:copy-of select="." copy-namespaces="no"/>
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>




</xsl:stylesheet>