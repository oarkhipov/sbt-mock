<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:ct="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/UpdateContractInfo/resp/10"
                xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/UpdateContractInfo/resp/10/Data"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <xsl:param name="dataFileName" select="'../../data/CRM/xml/UpdateContractInfoData.xml'"/>
    <xsl:param name="name" select="ns1:Message/*[local-name()='string1'][1]/text()"/>
    
    <xsl:template match="/">
        <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
        <xsl:call-template name="ExportContractInfoTemplate">
            <xsl:with-param name="data" select="$data"/>
            <xsl:with-param name="response">
                <xsl:choose>
                    <xsl:when test="count($data/rsd:response[@name=$linkedTag])=1">
                        <xsl:value-of select="$linkedTag"/>
                    </xsl:when>
                    <xsl:otherwise>default</xsl:otherwise>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>
        </xsl:element>
    </xsl:template>

    <xsl:template name="ExportContractInfoTemplate">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <xsl:element name="ns1:RqUID">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:RqUID"/>
        </xsl:element>
        <xsl:element name="ns1:RqTm">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:RqTm"/>
        </xsl:element>
        <xsl:element name="ns1:SPName">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:SPName"/>
        </xsl:element>
        <xsl:element name="ns1:SystemId">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:SystemId"/>
        </xsl:element>
        <xsl:element name="ns1:OperationName">
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:OperationName"/>
        </xsl:element>
        <xsl:element name="ns2:UpdateContractInfo">
            <xsl:element name="ns2:ContractID">
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ContractID"/>
            </xsl:element>
            <xsl:element name="ns2:ListOfError"/>
        </xsl:element>
        <xsl:if test="$data/rsd:response[@name=$response]/rsd:ErrorCode">
            <xsl:element name="ns1:ErrorCode">
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ErrorCode"/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="$data/rsd:response[@name=$response]/rsd:ErrorMessage">
            <xsl:element name="ns1:ErrorMessage">
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ErrorMessage"/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="$data/rsd:response[@name=$response]/rsd:ErrorDetails">
            <xsl:element name="ns1:ErrorDetails">
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ErrorDetails"/>
            </xsl:element>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>