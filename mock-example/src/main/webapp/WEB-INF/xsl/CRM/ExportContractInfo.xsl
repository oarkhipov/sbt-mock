<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:ct="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10"
                xmlns:tns="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10/Data"
                xmlns:ci="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/resp/10"
                xmlns:cid="http://sbrf.ru/prpc/kkmb/crm/ExportContractInfo/resp/10/Data"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <xsl:param name="name" select="//ci:ContractInfo/*//*[local-name()='OKITESTDEAL77'][1]/text()"/>
    <xsl:param name="dataFileName" select="'../../data/CRM/xml/ExportContractInfoResponseData.xml'"/>

    <xsl:template match="tns:Message">
        <xsl:variable name="data" select="document($dataFileName)/rsd:data"/>
        <xsl:variable name="linkedTag" select="$name"/>
        <xsl:element name="Message">
            <!-- Копируем всю необходимую информацию из полей заголовка
                используем для этого шаблон с параметрами -->
            <xsl:call-template name="HeaderTemplate">
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
            <!-- Вызов шаблона для заполения информации о контракте импортированном из CRM в BPM -->
            <xsl:call-template name="ExportContractTemplate">
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

            <!-- Заполнение информации об ошибке -->
        </xsl:element>
    </xsl:template>

    <xsl:template name="ExportContractTemplate">
    <xsl:param name="response"/>
    <xsl:param name="data"/>
    <xsl:element name="ContractInfo">
        <xsl:element name="ContractID">
            <xsl:value-of select="$data/rsd:response[@name=$response]/cid:ContractInfo/cid:ContractID"/>
        </xsl:element>
    </xsl:element>
</xsl:template>

    <xsl:template name="HeaderTemplate">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <tns:RqUID>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:RqUID"/>
        </tns:RqUID>
        <tns:RqTm>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:RqTm"/>
        </tns:RqTm>
        <tns:SPName>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:SPName"/>
        </tns:SPName>
        <tns:SystemId>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:SystemId"/>
        </tns:SystemId>
        <tns:OperationName>
            <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:OperationName"/>
        </tns:OperationName>
    </xsl:template>

</xsl:stylesheet>