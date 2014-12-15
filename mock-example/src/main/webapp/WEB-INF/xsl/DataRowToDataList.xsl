<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:dt="http://sbrf.ru/NCP/Data">

    <xsl:output method="xml" omit-xml-declaration="yes"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="*[local-name()='data']">
        <xsl:for-each select="./*[local-name()='response']"><xsl:text><xsl:value-of select="./@name"/>
</xsl:text>
            </xsl:for-each>
            <xsl:for-each select="./*[local-name()='request']">
                <xsl:text><xsl:value-of select="./@name"/>
</xsl:text>
            </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>