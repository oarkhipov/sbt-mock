<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:param name="dataFile" select="."/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="*">
        <xsl:variable name="fileparh" select="string(concat('../data/',$dataFile))"/>
        <xsl:variable name="data" select="document($fileparh)/*[local-name()='data']"/>
        <xsl:variable name="newName" select="./@name"/>
        <xsl:element name="data">
            <!--xsl:attribute name="xmlns">
                <xsl:value-of select="./@xmlns"/>
            </xsl:attribute-->
            <xsl:copy-of select="$data/*[(local-name()='request' or local-name()='response') and @name!=$newName]"/>
            <xsl:copy-of select="."/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>