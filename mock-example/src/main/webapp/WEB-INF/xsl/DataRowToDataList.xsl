<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:dt="http://sbrf.ru/NCP/Data">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="*[local-name()='data']">
        <xsl:element name="dt:data">
            <xsl:for-each select="./*[local-name()='response']">
                <xsl:element name="dt:response">
                    <xsl:attribute name="name">
                        <xsl:value-of select="./@name"/>
                    </xsl:attribute>
                </xsl:element>
            </xsl:for-each>
            <xsl:for-each select="./*[local-name()='request']">
                <xsl:element name="dt:request">
                    <xsl:attribute name="name">
                        <xsl:value-of select="./@name"/>
                    </xsl:attribute>
                </xsl:element>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>