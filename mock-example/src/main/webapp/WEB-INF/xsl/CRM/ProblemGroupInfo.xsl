<xsl:stylesheet xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/ProblemGroup/req/10/Data"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/req/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/ProblemGroup/req/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    <xsl:param name="name" select="all"/>
    <xsl:param name="operationName" select="string('ProblemGroup.Req')"/>

    <xsl:template match="/">
        <xsl:param name="data" select="//rsd:data"/>
        <xsl:param name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
            <xsl:call-template name="ProblemGroupRq">
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

    <xsl:template match="rsd:ListOfContract">
        <xsl:element name="ns2:ContractId"><xsl:value-of select="./rsd:ContractId"/></xsl:element>
    </xsl:template>

    <xsl:template match="rsd:ProblemGroup">
        <xsl:element name="ns2:ProblemGroup">
            <xsl:element name="ns2:OrgID"><xsl:value-of select="./rsd:OrgID"/></xsl:element>
            <xsl:if test="./rsd:ProblemGroup">
                <xsl:element name="ns2:ProblemGroup"><xsl:value-of select="./rsd:ProblemGroup"/></xsl:element>
            </xsl:if>
            <xsl:if test="./rsd:PotencialProblemGroup">
                <xsl:element name="ns2:PotencialProblemGroup"><xsl:value-of select="./rsd:PotencialProblemGroup"/></xsl:element>
            </xsl:if>
            <xsl:element name="ns2:ListOfContract">
                <xsl:apply-templates select="./rsd:ListOfContract"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="ProblemGroupRq">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <xsl:element name="ns1:RqUID"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqUID"/></xsl:element>
        <xsl:element name="ns1:RqTm"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqTm"/></xsl:element>
        <xsl:element name="ns1:SPName"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SPName"/></xsl:element>
        <xsl:element name="ns1:SystemId"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SystemId"/></xsl:element>
        <xsl:element name="ns1:OperationName"><xsl:value-of select="$operationName"/></xsl:element>
        <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:ProblemGroup"/>
    </xsl:template>

</xsl:stylesheet>