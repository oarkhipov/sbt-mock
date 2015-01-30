<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns1="http://sbrf.ru/prpc/kkmb/crm/Header/resp/10"
                version="1.0">

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes" standalone="yes"/>

    <xsl:template match="/">
        <ns1:Message>
            <xsl:element name="ns1:RqUID"><xsl:value-of select="./ns1:RqUID"/></xsl:element>
            <ns1:RqTm></ns1:RqTm>
            <ns1:SPName></ns1:SPName>
            <ns1:SystemId></ns1:SystemId>
            <ns1:OperationName></ns1:OperationName>
        </ns1:Message>
    </xsl:template>

</xsl:stylesheet>