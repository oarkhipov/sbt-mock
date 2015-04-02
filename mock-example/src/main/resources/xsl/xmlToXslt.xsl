<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>

    <xsl:template match="node()">
        <xsl:element name="xsl:element">
            <xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
            <xsl:for-each select="@*">
                <xsl:element name="xsl:attribute">
                    <xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:element>
            </xsl:for-each>
            <xsl:apply-templates select="*"/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>