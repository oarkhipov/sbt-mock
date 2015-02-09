<xsl:stylesheet xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/ContractClose/req/10/Data"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/req/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/ContractClose/req/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <!--опускаем строку 'xml version="1.0" encoding="UTF-8"'. С ней не работает MQ очередь-->
    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    <xsl:param name="name" select="all"/>
    <xsl:param name="operationName" select="string('CloseContractInfo.REQ')"/>

    <xsl:template match="/">
        <xsl:variable name="data" select="//rsd:data"/>
        <xsl:variable name="linkedTag" select="$data"/>
        <xsl:element name="ns1:Message">
            <xsl:call-template name="CloseContractInfoReq">
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

    <xsl:template match="rsd:AgrGroup">
        <xsl:if test="./rsd:ProductIntID">
            <xsl:element name="ns2:ProductIntID"><xsl:value-of select="./rsd:ProductIntID"/></xsl:element>
        </xsl:if>
        <xsl:if test="./rsd:AgrNum">
            <xsl:element name="ns2:AgrNum"><xsl:value-of select="./rsd:AgrNum"/></xsl:element>
        </xsl:if>
        <xsl:if test="./rsd:AgrDate">
            <xsl:element name="ns2:AgrDate"><xsl:value-of select="./rsd:AgrDate"/></xsl:element>
        </xsl:if>
    </xsl:template>

    <xsl:template match="rsd:ContractClose">
        <xsl:element name="ns2:ContractClose">
            <xsl:element name="ns2:ContractID"><xsl:value-of select="./rsd:ContractID"/></xsl:element>
            <xsl:element name="ns2:ContractStatus"><xsl:value-of select="./rsd:ContractStatus"/></xsl:element>
            <xsl:if test="./rsd:CauseClosed">
                <xsl:element name="ns2:CauseClosed"><xsl:value-of select="./rsd:CauseClosed"/></xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ListOfArgGroup">
                <xsl:if test="./rsd:ListOfAgrGroup/rsd:AgrGroup">
                    <xsl:element name="ns2:ArgGroup"><xsl:apply-templates select="./rsd:ListOfAgrGroup/rsd:AgrGroup"/></xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="CloseContractInfoReq">
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <xsl:element name="ns1:RqUID"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqUID"/></xsl:element>
        <xsl:element name="ns1:RqTm"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqTm"/></xsl:element>
        <xsl:element name="ns1:SPName"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SPName"/></xsl:element>
        <xsl:element name="ns1:SystemId"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SystemId"/></xsl:element>
        <xsl:element name="ns1:OperationName"><xsl:value-of select="$operationName"/></xsl:element>
        <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:ContractClose"/>
    </xsl:template>

</xsl:stylesheet>