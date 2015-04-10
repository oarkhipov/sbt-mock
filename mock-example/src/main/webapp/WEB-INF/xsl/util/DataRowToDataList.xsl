<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:dt="http://sbrf.ru/NCP/Data">
<!-- по -->
    <xsl:output method="xml" omit-xml-declaration= "yes"/>
    <xsl:param name="name" select="all"/>
    <!--Prepare data and section of data XML-->
    <xsl:template match="*[local-name()='data']">
        <xsl:choose>
            <xsl:when test="count(./*[(local-name()='response' or  local-name()='request') and @name=$name])=1">
                <xsl:copy-of select="./*[(local-name()='response' or  local-name()='request') and @name=$name]"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="./*[local-name()='response']"><xsl:value-of select="./@name"/><xsl:text>
</xsl:text>
                </xsl:for-each>
                <xsl:for-each select="./*[local-name()='request']">
                <xsl:value-of select="./@name"/><xsl:text>
</xsl:text>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>