<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <xsl:param name="name"
               select="'default'"/>
    <xsl:param name="dataFile" select="."/>
    <xsl:param name="dataFileName"
               select="'../../data/AMRLiRT/xml/CalculateLGDData.xml'"/>
    <xsl:param name="createEmptyData"
               select="'false'"/>
    <xsl:param name="replace"
               select="'false'"/>
    <xsl:param name="type"
               select="'response'"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="/">
        <xsl:variable name="fileparh" select="string(concat('../data/',$dataFile))"/>
        <xsl:variable name="data" select="document($dataFileName)/*[local-name()='data']"/>
        <xsl:variable name="dataNS" select="namespace-uri($data)"/>
        <xsl:element name="data" namespace="{$dataNS}">
            <xsl:if test="$createEmptyData='false'">
                <xsl:if test="$replace='false'">
                    <xsl:copy-of select="$data/*[(local-name()='request' or local-name()='response')][@name!=$name]"/>
                </xsl:if>
                <xsl:apply-templates select="//soap-env:Body" mode="add">
                    <xsl:with-param name="ns" select="$dataNS"/>
                </xsl:apply-templates>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="soap-env:Body" mode="add">
        <xsl:param name="ns"/>
        <xsl:element name="{$type}" namespace="{$ns}">
            <xsl:attribute name="name"><xsl:value-of select="$name"/></xsl:attribute>
            <xsl:apply-templates select="./*/*" mode="copyNopNS">
                <xsl:with-param name="ns" select="$ns"/>
            </xsl:apply-templates>
        </xsl:element>
    </xsl:template>

    <xsl:template match="node()" mode="copyNopNS">
        <xsl:param name="ns"/>
        <xsl:element name="{local-name(.)}" namespace="{$ns}">
            <xsl:apply-templates select="./*" mode="copyNopNS">
                <xsl:with-param name="ns" select="$ns"/>
            </xsl:apply-templates>
            <xsl:if test="count(child::*)=0">
                <xsl:value-of select="text()"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="comment()" mode="copyNopNS">
        <xsl:copy-of select="."/>
    </xsl:template>

</xsl:stylesheet>