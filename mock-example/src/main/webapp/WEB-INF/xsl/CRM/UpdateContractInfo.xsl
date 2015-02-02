<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                xmlns:tns="http://sbrf.ru/prpc/kkmb/crm/UpdateContractInfo/resp/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/UpdateContractInfo/resp/10/Data"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <xsl:import href="../util/KKMBCommonData.xsl"/>

    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <xsl:param name="dataFileName" select="'../../data/CRM/xml/UpdateContractInfoData.xml'"/>
    <xsl:param name="name" select="ns1:Message/*//*[local-name()='RqUID'][1]/text()"/>
    <xsl:param name="OperationName" select="string('UpdateContractInfo')"/>

    <xsl:template match="ns1:Message">
        <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
        <xsl:call-template name="UpdateContractInfoTemplate">
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

    <xsl:template name="UpdateContractInfoTemplate">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <xsl:call-template name="HeaderRequestValuesTemplate">
            <xsl:with-param name="RqUID" select="$data/rsd:response[@name=$response]/rsd:RqUID"/>
            <xsl:with-param name="RqTm" select="$data/rsd:response[@name=$response]/rsd:RqTm"/>
            <xsl:with-param name="SystemId" select="$data/rsd:response[@name=$response]/rsd:SPName"/>
            <xsl:with-param name="SPName" select="$data/rsd:response[@name=$response]/rsd:SystemId"/>
            <xsl:with-param name="OperationName" select="$OperationName"/>
        </xsl:call-template>
        <ns2:UpdateContratcInfo>
        <xsl:element name="ns2:ContractID">
        <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ContractID"/>
        </xsl:element>
        <xsl:element name="ns2:ListOfError">
        </xsl:element>
        </ns2:UpdateContratcInfo>
        <xsl:if test="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ErrorCode">
            <xsl:call-template name="ErrorInformationTemplate">
                <xsl:with-param name="ErrorCode" select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ErrorCode"/>
                <xsl:with-param name="ErrorMessage" select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ErrorMessage"/>
                <xsl:with-param name="ErrorDetails" select="$data/rsd:response[@name=$response]/rsd:UpdateContractInfo/rsd:ErrorDetails"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>