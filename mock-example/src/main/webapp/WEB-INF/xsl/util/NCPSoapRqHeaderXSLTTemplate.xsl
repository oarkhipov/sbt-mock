<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap="http://sbrf.ru/NCP/esb/envelope/">

    <!--заголовок из дата-файла-->
    <xsl:template name="NCPHeader" xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/1.03/Data/">
        <xsl:param name="response" select="'default'"/>
        <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410')"/>
        <xsl:param name="operation-name"/>

        <!-- Optional params for optional header values -->
        <xsl:param name="id" select="null"/>
        <xsl:param name="correlation-id" select="null"/>
        <xsl:param name="eis-name" select="null"/>
        <xsl:param name="system-id" select="null"/>
        <xsl:param name="operation-version" select="null"/>
        <xsl:param name="user-id" select="null"/>
        <xsl:param name="user-name" select="null"/>
        <xsl:variable name="defaultId" select="'defaultId'"/>
        <soap:Header>
            <soap:message-id>
                <xsl:choose>
                    <xsl:when test="$id!='null'"><xsl:value-of select="$id"/></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='message-id']"><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='message-id']"/></xsl:when>
                    <xsl:when test="/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='message-id']"><xsl:value-of select="/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='message-id']"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
                </xsl:choose>
            </soap:message-id>
            <soap:request-time>
                <xsl:choose>
                    <xsl:when test="/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='request-time']"><xsl:value-of select="/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='request-time']"/></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='request-time']"><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='request-time']"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$timestamp"/></xsl:otherwise>
                </xsl:choose>
            </soap:request-time>
            <soap:operation-name><xsl:value-of select="$operation-name"/></soap:operation-name>

            <xsl:choose>
                <xsl:when test="$correlation-id!='null'"><soap:correlation-id><xsl:value-of select="$correlation-id"/></soap:correlation-id></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='correlation-id']"><soap:correlation-id><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='correlation-id']"/></soap:correlation-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$eis-name!='null'"><soap:eis-name><xsl:value-of select="$eis-name"/></soap:eis-name></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='eis-name']"><soap:eis-name><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='eis-name']"/></soap:eis-name></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$system-id!='null'"><soap:system-id><xsl:value-of select="$system-id"/></soap:system-id></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='system-id']"><soap:system-id><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='system-id']"/></soap:system-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$operation-version!='null'"><soap:operation-version><xsl:value-of select="$operation-version"/></soap:operation-version></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='operation-version']"><soap:operation-version><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='operation-version']"/></soap:operation-version></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-id!='null'"><soap:user-id><xsl:value-of select="$user-id"/></soap:user-id></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='user-id']"><soap:user-id><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='user-id']"/></soap:user-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-name!='null'"><soap:user-name><xsl:value-of select="$user-name"/></soap:user-name></xsl:when>
                <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='user-id']"><soap:user-name><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='user-id']"/></soap:user-name></xsl:when>
            </xsl:choose>
        </soap:Header>
    </xsl:template>

</xsl:stylesheet>