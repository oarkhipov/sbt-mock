<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:ct="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/req/10"
                xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/req/10/Data"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <!--<xsl:param name="dataFileName" select="'../../data/CRM/xml/ExportContractInfoData.xml'"/>-->
    <xsl:param name="name" select="all"/>

    <xsl:template match="/">
        <xsl:variable name="data" select="//rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
            <xsl:call-template name="UpdateContractRq">
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
        </xsl:element>
    </xsl:template>

    <xsl:template name="UpdateContractRq">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <xsl:element name="ns1:ContractInfo">
            
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>