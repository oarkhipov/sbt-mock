<xsl:stylesheet  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                 version="2.0">

    <!-- Заголовки сообщений -->
    <!-- Заголовок ответа -->
    <xsl:template name="HeaderRequestValuesTemplate"
                  xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10">
        <xsl:param name="RqUID"/>
        <xsl:param name="RqTm"/>
        <xsl:param name="SPName"/>
        <xsl:param name="SystemId"/>
        <xsl:param name="OperationName"/>

        <ns1:RqUID><xsl:value-of select="$RqUID"/></ns1:RqUID>
        <ns1:RqTm><xsl:value-of select="$RqTm"/></ns1:RqTm>
        <ns1:SPName><xsl:value-of select="$SPName"/></ns1:SPName>
        <ns1:SystemId><xsl:value-of select="$SystemId"/></ns1:SystemId>
        <ns1:OperationName><xsl:value-of select="$OperationName"/></ns1:OperationName>
    </xsl:template>

    <!-- Заголовок запроса -->

    <!-- Информация об ошибке -->
    <xsl:template name="ErrorInformationTemplate" xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10">
        <xsl:param name="ErrorCode"/>
        <xsl:param name="ErrorMessage"/>
        <xsl:param name="ErrorDetails"/>

        <ns1:ErrorCode><xsl:value-of select="$ErrorCode"/></ns1:ErrorCode>
        <ns1:ErrorMessage><xsl:value-of select="$ErrorMessage"/></ns1:ErrorMessage>
        <ns1:ErrorDetails><xsl:value-of select="$ErrorDetails"/></ns1:ErrorDetails>
    </xsl:template>
    <!-- ============================================== -->

    <!-- ListOfErrorTemplate -->
    <xsl:template name="ListOfErrorTemplate" xmlns:ns2="http://sbrf.ru/prpc/kkmb/crm/CommonTypes/10">
        <xsl:param name="Error" select="null"/>
        <xsl:param name="ErrorCode" select="null"/>
        <xsl:param name="ErrorMessage" select="null"/>
            <ns2:Error>
                <!--<ns2:ErrorCode><xsl:value-of select="$ErrorCode"/></ns2:ErrorCode>-->
                <ns2:ErrorMessage></ns2:ErrorMessage>
            </ns2:Error>
    </xsl:template>
    <!-- ============================================== -->

</xsl:stylesheet>