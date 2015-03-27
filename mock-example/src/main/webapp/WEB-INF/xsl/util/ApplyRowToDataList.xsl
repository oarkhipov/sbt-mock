<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>
    <xsl:param name="dataFile" select="."/>
    <xsl:param name="dataFileName"
               select="'../../data/AMRLiRT/xml/CalculateLGDData.xml'"/>
    <xsl:param name="createEmptyData"
               select="false()"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="/">
        <xsl:variable name="fileparh" select="string(concat('../data/',$dataFile))"/>
        <xsl:variable name="data" select="document($dataFileName)/*[local-name()='data']"/>
        <xsl:variable name="newName" select="//*[(local-name()='request' or local-name()='response')]/@name"/>
        <xsl:element name="data" namespace="">
            <xsl:copy-of select="$data/*[(local-name()='request' or local-name()='response') and @name!=$newName]"/>
            <xsl:copy-of select="."/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>