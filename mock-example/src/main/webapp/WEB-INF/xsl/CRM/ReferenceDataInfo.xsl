<xsl:stylesheet xmlns:rsd="http://sbrf.ru/prpc/kkmb/crm/ReferenceData/req/10/Data"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/req/10"
                xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/ReferenceData/req/10"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    <xsl:param name="name" select="all"/>
    <xsl:param name="operationName" select="string('UpdateReferenceData.REQ')"/>

    <xsl:template match="/">
        <xsl:param name="data" select="//rsd:data"/>
        <xsl:param name="linkedTag" select="$name"/>
        <xsl:element name="ns1:Message">
            <xsl:call-template name="UpdateReferenceDataRq">
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

    <xsl:template match="rsd:Value">
        <xsl:element name="ns2:ValueName"><xsl:value-of select="./rsd:ValueName"/></xsl:element>
        <xsl:element name="ns2:ValueID"><xsl:value-of select="./rsd:ValueID"/></xsl:element>
        <xsl:if test="./rsd:ParentValueID">
            <xsl:element name="ns2:ParentValueID"><xsl:value-of select="./rsd:ParentValueID"/></xsl:element>
        </xsl:if>
    </xsl:template>

    <xsl:template match="rsd:ListType">
        <xsl:element name="ns2:ListID"><xsl:value-of select="./rsd:ListID"/></xsl:element>
        <xsl:if test="./rsd:ParentListID">
            <xsl:element name="ns2:ParentListID"><xsl:value-of select="./rsd:ParentListID"/></xsl:element>
        </xsl:if>
        <xsl:element name="ns2:ListOfValue">
            <xsl:element name="ns2:Value">
                <xsl:apply-templates select="./rsd:ListOfValue/rsd:Value"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="rsd:LimitMB">
        <xsl:element name="ns2:ProductID"><xsl:value-of select="./rsd:ProductId"/></xsl:element>
        <xsl:element name="ns2:CreditPurposeID"><xsl:value-of select="./rsd:CreditPurposeID"/></xsl:element>
    </xsl:template>

    <xsl:template match="rsd:ReferenceData">
        <xsl:element name="ns2:ReferenceData">
            <xsl:element name="ns2:ListOfListType">
                <xsl:if test="./rsd:ListOfListType/rsd:ListType">
                    <xsl:element name="ns2:ListType"><xsl:value-of select="./rsd:ListOfListType/rsd:ListType"/></xsl:element>
                </xsl:if>
            </xsl:element>
            <xsl:element name="ns2:ListOfLimitMB">
                <xsl:if test="./rsd:ListOfLimitMB/rsd:LimitMB">
                    <xsl:element name="ns2:LimitMB">
                        <xsl:apply-templates select="./rsd:ListOfLimitMB/rsd:LimitMB"/>
                    </xsl:element>
                </xsl:if>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template name="UpdateReferenceDataRq">
        <xsl:param name="data"/>
        <xsl:param name="response"/>
        <xsl:element name="ns1:RqUID"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqUID"/></xsl:element>
        <xsl:element name="ns1:RqTm"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:RqTm"/></xsl:element>
        <xsl:element name="ns1:SPName"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SPName"/></xsl:element>
        <xsl:element name="ns1:SystemId"><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:SystemId"/></xsl:element>
        <xsl:element name="ns1:OperationName"><xsl:value-of select="$operationName"/></xsl:element>
        <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:ReferenceData"/>
    </xsl:template>

</xsl:stylesheet>
