<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:crmct="http://sbrf.ru/NCP/CRM/CommonTypes/"
                xmlns:tns="http://sbrf.ru/NCP/CRM/ForceSignalRq/1.03/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/1.03/Data/"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <xsl:import href="../util/NCPSoapRqHeaderXSLTTemplate.xsl"/>
    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    <xsl:param name="name" select="all"/>
    <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
    <xsl:param name="id" select="null"/>
    <!--Optional params for optional header values-->
    <xsl:param name="correlation-id" select="null"/>
    <xsl:param name="eis-name" select="null"/>
    <xsl:param name="system-id" select="null"/>
    <xsl:param name="operation-version" select="null"/>
    <xsl:param name="user-id" select="null"/>
    <xsl:param name="user-name" select="null"/>

    <xsl:template match="/">
        <xsl:variable name="data" select="//rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="soap-env:Envelope">
            <xsl:call-template name="NCPHeader">
                <xsl:with-param name="response">
                    <xsl:choose>
                        <xsl:when test="count(./rsd:request[@name=$linkedTag])=1">
                            <xsl:value-of select="$linkedTag"/>
                        </xsl:when>
                        <xsl:otherwise>default</xsl:otherwise>
                    </xsl:choose>
                </xsl:with-param>
                <xsl:with-param name="timestamp" select="$timestamp"/>
                <xsl:with-param name="id" select="$id"/>
                <xsl:with-param name="operation-name" select="string('ForceSignalRq')"/>
                <xsl:with-param name="correlation-id" select="$correlation-id"/>
                <xsl:with-param name="eis-name" select="$eis-name"/>
                <xsl:with-param name="system-id" select="$system-id"/>
                <xsl:with-param name="operation-version" select="$operation-version"/>
                <xsl:with-param name="user-id" select="$user-id"/>
                <xsl:with-param name="user-name" select="$user-name"/>
            </xsl:call-template>
            <soap-env:Body>
                <xsl:call-template name="ForceSignalRq">
                    <xsl:with-param name="data" select="$data"/>
                    <xsl:with-param name="response">
                        <xsl:choose>
                            <xsl:when test="count($data/rsd:request[@name=$linkedTag])=1">
                                <xsl:value-of select="$linkedTag"/>
                            </xsl:when>
                            <xsl:otherwise>default</xsl:otherwise>
                        </xsl:choose>
                    </xsl:with-param>
                </xsl:call-template>
            </soap-env:Body>
        </xsl:element>
    </xsl:template>

    <xsl:template name="ForceSignalRq">
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <xsl:element name="crm:ForceSignalRq">
            <tns:contractID>
                <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:contractID"/>
            </tns:contractID>
            <xsl:if test="$data/rsd:request[@name=$response]/rsd:contractBPMID">
                <tns:contractBPMID>
                    <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:contractBPMID"/>
                </tns:contractBPMID>
            </xsl:if>
            <xsl:if test="$data/rsd:request[@name=$response]/rsd:status">
                <tns:status>
                    <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:status"/>
                </tns:status>
            </xsl:if>
            <tns:comment>
                <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:comment"/>
            </tns:comment>
            <tns:requestType>
                <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:requestType"/>
            </tns:requestType>
            <tns:responsiblePersonID>
                <xsl:value-of select="$data/rsd:request[@name=$response]/rsd:responsiblePersonID"/>
            </tns:responsiblePersonID>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
