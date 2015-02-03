<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:mock="http://sbrf.ru/mockService"><!--TODO заменить mock на namespace конфига -->

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:include href="xsltFunctions.xsl"/>

    <xsl:param name="name"
               select="'default'"/>
    <xsl:param name="dataFile" select="."/>
    <xsl:param name="dataFileName"
               select="''"/>
    <xsl:param name="system"
               select="'CRM'"/>
    <xsl:param name="dataFilePath"
               select="concat('../../data/',$system,'/xml/',$dataFileName)"/>
    <xsl:param name="createEmptyData"
               select="'false'"/>
    <xsl:param name="replace"
               select="'false'"/>
    <xsl:param name="type"
               select="'response'"/>
    <xsl:param name="dataNsUrl"
               select="mock:addDataToNamespaceUrl(/*[local-name()='Envelope']/*[local-name()='Body']/descendant-or-self::*[last()]/namespace-uri())"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="/">
        <xsl:variable name="fileparh" select="string(concat('../data/',$dataFile))"/>
        <xsl:variable name="data" select="document($dataFilePath)/*[local-name()='data']"/>
        <xsl:variable name="dataNS" select="if ($replace='false') then $data/namespace-uri() else $dataNsUrl"/>
        <xsl:element name="data" namespace="{$dataNS}">
            <xsl:if test="$createEmptyData='false'">
                <xsl:if test="$replace='false'">
                    <xsl:copy-of select="$data/*[(local-name()='request' or local-name()='response')][@name!=$name]"/>
                </xsl:if>
                <xsl:apply-templates select="//*[local-name()='Body']" mode="add">
                    <xsl:with-param name="ns" select="$dataNS"/>
                </xsl:apply-templates>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="*[local-name()='Body']" mode="add">
        <xsl:param name="ns"/>
        <xsl:element name="{$type}" namespace="{$ns}">
            <xsl:attribute name="name"><xsl:value-of select="$name"/></xsl:attribute>
            <xsl:element name="SoapHeader" namespace="{$ns}">
                <xsl:apply-templates select="//*[local-name()='Header']/*" mode="copyNopNS">
                    <xsl:with-param name="ns" select="$ns"/>
                </xsl:apply-templates>
            </xsl:element>
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