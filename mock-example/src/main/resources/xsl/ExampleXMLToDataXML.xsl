<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

    <xsl:template match="soap-env:Body">
        <xsl:variable name="dataNS" select="concat(namespace-uri(*/*[1]),'Data/')"/>
        <xsl:element name="data" namespace="{$dataNS}">
            <xsl:element name="response" namespace="{$dataNS}">
                <xsl:attribute name="name">default</xsl:attribute>
                <xsl:apply-templates select="*/*" mode="copy">
                    <xsl:with-param name="dataNS" select="$dataNS"/>
                </xsl:apply-templates>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="node()" mode="copy">
        <xsl:param name="dataNS"/>
        <xsl:variable name="tagName" select="local-name()"/>
        <xsl:if test="$tagName!=''">
            <xsl:element name="{$tagName}" namespace="{$dataNS}">
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates select="node()" mode="copy">
                    <xsl:with-param name="dataNS" select="$dataNS"/>
                </xsl:apply-templates>
                <xsl:value-of select="text()[normalize-space()]"/>
                <xsl:if test="text()[normalize-space()]='string'"><xsl:value-of select="position()"/></xsl:if>
            </xsl:element>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>