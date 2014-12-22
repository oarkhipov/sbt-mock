<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/">

    <xsl:param name="defaultId" select="string('defaultId')"/>

    <xsl:template name="NCPHeader">
        <xsl:param name="response"/>
        <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
        <xsl:param name="operation-name"/>

        <!-- Optional params for optional header values -->
        <xsl:param name="id" select="null"/>
        <xsl:param name="correlation-id" select="null"/>
        <xsl:param name="eis-name" select="null"/>
        <xsl:param name="system-id" select="null"/>
        <xsl:param name="operation-version" select="null"/>
        <xsl:param name="user-id" select="null"/>
        <xsl:param name="user-name" select="null"/>
        <soap-env:Header>
            <soap-env:message-id>
                <xsl:choose>
                    <xsl:when test="$id!='null'"><xsl:value-of select="$id"/></xsl:when>
                    <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:message-id"><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:message-id"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
                </xsl:choose>
            </soap-env:message-id>
            <soap-env:request-time><xsl:value-of select="$timestamp"/></soap-env:request-time>
            <soap-env:operation-name><xsl:value-of select="$operation-name"/></soap-env:operation-name>

            <xsl:choose>
                <xsl:when test="$correlation-id!='null'"><soap-env:correlation-id><xsl:value-of select="$correlation-id"/></soap-env:correlation-id></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:correlation-id"><soap-env:correlation-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:correlation-id"/></soap-env:correlation-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$eis-name!='null'"><soap-env:eis-name><xsl:value-of select="$eis-name"/></soap-env:eis-name></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:eis-name"><soap-env:eis-name><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:eis-name"/></soap-env:eis-name></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$system-id!='null'"><soap-env:system-id><xsl:value-of select="$system-id"/></soap-env:system-id></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:system-id"><soap-env:system-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:system-id"/></soap-env:system-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$operation-version!='null'"><soap-env:operation-version><xsl:value-of select="$operation-version"/></soap-env:operation-version></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:operation-version"><soap-env:operation-version><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:operation-version"/></soap-env:operation-version></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-id!='null'"><soap-env:user-id><xsl:value-of select="$user-id"/></soap-env:user-id></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"><soap-env:user-id><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"/></soap-env:user-id></xsl:when>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="$user-name!='null'"><soap-env:user-name><xsl:value-of select="$user-name"/></soap-env:user-name></xsl:when>
                <xsl:when test="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"><soap-env:user-name><xsl:value-of select="./rsd:request[@name=$response]/rsd:SoapHeader/rsd:user-id"/></soap-env:user-name></xsl:when>
            </xsl:choose>
        </soap-env:Header>
    </xsl:template>

</xsl:stylesheet>