<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template name="KD4Header" xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/1.03/Data/"
                  xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:kd4="http://www.ibm.com/KD4Soap"
                  xmlns:mq="http://sbrf.ru/prpc/mq/headers">
        <xsl:param name="response"/>
        <xsl:param name="request-time" select="'2014-12-16T17:55:06.410'"/>
        <xsl:param name="operation-name"/>
        <xsl:param name="message-id" select="''"/>

        <!-- Optional params for optional header values -->
        <xsl:param name="kd4header" select="''"/>
        <xsl:param name="correlation-id" select="''"/>
        <xsl:param name="eis-name" select="''"/>
        <xsl:param name="system-id" select="''"/>
        <xsl:param name="operation-version" select="''"/>
        <xsl:param name="user-id" select="''"/>
        <xsl:param name="user-name" select="''"/>
        <xsl:param name="proc-inst-tb" select="''"/>
        <xsl:variable name="defaultId" select="'defaultId'"/>

        <soap:Header>
            <xsl:if test="$kd4header!=''">
                <kd4:KD4SoapHeaderV2><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='KD4SoapHeaderV2']"/></kd4:KD4SoapHeaderV2>
            </xsl:if>
            <mq:AsyncHeader>
                <!--<xsl:comment>test <xsl:value-of select="//*[local-name()='request' or local-name()='response']/local-name()"/></xsl:comment>-->
                <!--<xsl:comment>test <xsl:value-of select="$response"/></xsl:comment>-->
                <mq:message-id>
                    <xsl:choose>
                        <xsl:when test="$message-id!=''"><xsl:value-of select="$message-id"/></xsl:when>
                        <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='message-id']"><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='message-id']"/></xsl:when>
                        <xsl:when test="/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='AsyncHeader']/*[local-name()='message-id']"><xsl:value-of select="/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='AsyncHeader']/*[local-name()='message-id']"/></xsl:when>
                        <xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
                    </xsl:choose>
                </mq:message-id>
                <mq:request-time><xsl:value-of select="$request-time"/></mq:request-time>
                <xsl:choose>
                    <xsl:when test="$correlation-id!=''"><mq:correlation-id><xsl:value-of select="$correlation-id"/></mq:correlation-id></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='correlation-id']"><mq:correlation-id><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='correlation-id']"/></mq:correlation-id></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$eis-name!=''"><mq:eis-name><xsl:value-of select="$eis-name"/></mq:eis-name></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='eis-name']"><mq:eis-name><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='eis-name']"/></mq:eis-name></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$system-id!=''"><mq:system-id><xsl:value-of select="$system-id"/></mq:system-id></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='system-id']"><mq:system-id><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='system-id']"/></mq:system-id></xsl:when>
                </xsl:choose>
                <mq:operation-name><xsl:value-of select="$operation-name"/></mq:operation-name>
                <xsl:choose>
                    <xsl:when test="$operation-version!=''"><mq:operation-version><xsl:value-of select="$operation-version"/></mq:operation-version></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='operation-version']"><mq:operation-version><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='operation-version']"/></mq:operation-version></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$user-id!=''"><mq:user-id><xsl:value-of select="$user-id"/></mq:user-id></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='user-id']"><mq:user-id><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='user-id']"/></mq:user-id></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$user-name!=''"><mq:user-name><xsl:value-of select="$user-name"/></mq:user-name></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='user-id']"><mq:user-name><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='user-id']"/></mq:user-name></xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test="$proc-inst-tb!=''"><mq:proc-inst-tb><xsl:value-of select="$proc-inst-tb"/></mq:proc-inst-tb></xsl:when>
                    <xsl:when test="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='proc-inst-tb']"><mq:proc-inst-tb><xsl:value-of select="//*[local-name()='request' or local-name()='response'][@name=$response]/*[local-name()='SoapHeader']/*[local-name()='AsyncHeader']/*[local-name()='proc-inst-tb']"/></mq:proc-inst-tb></xsl:when>
                </xsl:choose>
            </mq:AsyncHeader>
        </soap:Header>
    </xsl:template>

</xsl:stylesheet>