<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="operationsXSL" select="''"/>
    <xsl:variable name="operationXSLStylesheet" select="document($operationsXSL)/xsl:stylesheet"/>

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <xsl:template match="/">
        <xsl:apply-templates select="$operationXSLStylesheet" mode="copy"/>
    </xsl:template>

    <xsl:template match="node()|@*" mode="copy">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*|comment()" mode="copy"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>