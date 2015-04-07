<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="KKMBHeader"
                  xmlns:msg="http://schemas.xmlsoap.org/soap/envelope/">
        <xsl:param name="response"/>
        <xsl:param name="RqUID"/>
        <xsl:param name="RqTm" select="'2014-12-16T17:55:06.410'"/>
        <xsl:param name="SPName"/>
        <xsl:param name="OperationName"/>
        <xsl:param name="system-id"/>

        <!--<xsl:comment>test <xsl:value-of select="//*[local-name()='request' or local-name()='response']/local-name()"/></xsl:comment>-->
        <!--<xsl:comment>test <xsl:value-of select="$response"/></xsl:comment>-->
        <msg:RqUID>
            <xsl:choose>
                <xsl:when test="$RqUID!=''"><xsl:value-of select="$RqUID"/></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='RqUID']"><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='RqUID']"/></xsl:when>
                <xsl:when test="/*[local-name()='Message']/*[local-name()='RqUID']"><xsl:value-of select="/*[local-name()='Message']/*[local-name()='RqUID']"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="''"/></xsl:otherwise>
            </xsl:choose>
        </msg:RqUID>
        <msg:RqTm><xsl:value-of select="$RqTm"/></msg:RqTm>
        <msg:SPName>
            <xsl:choose>
                <xsl:when test="$SPName!=''"><xsl:value-of select="$SPName"/></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='SPName']"><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='SPName']"/></xsl:when>
                <xsl:when test="/*[local-name()='Message']/*[local-name()='SPName']"><xsl:value-of select="/*[local-name()='Message']/*[local-name()='SPName']"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="''"/></xsl:otherwise>
            </xsl:choose>
        </msg:SPName>
        <msg:OperationName><xsl:value-of select="$OperationName"/></msg:OperationName>
        <msg:system-id>
            <xsl:choose>
                <xsl:when test="$system-id!=''"><xsl:value-of select="$system-id"/></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='system-id']"><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='system-id']"/></xsl:when>
                <xsl:when test="/*[local-name()='Message']/*[local-name()='system-id']"><xsl:value-of select="/*[local-name()='Message']/*[local-name()='system-id']"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="''"/></xsl:otherwise>
            </xsl:choose>
        </msg:system-id>
        
    </xsl:template>


</xsl:stylesheet>