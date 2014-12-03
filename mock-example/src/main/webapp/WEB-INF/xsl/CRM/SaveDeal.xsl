<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0" />

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="soap-env:Envelope">
        <xsl:element name="soap-env:Envelope">
            <xsl:copy-of select="soap-env:Header"/>
            <soap-env:Body>
                <xsl:call-template name="SaveDeal" />
            </soap-env:Body>
        </xsl:element>
    </xsl:template>

    <xsl:template name="SaveDeal">
        <ns1:saveDealRs xmlns:ns1="http://sbrf.ru/NCP/CRM/" xmlns:svd="http://sbrf.ru/NCP/CRM/SaveDealRs/">
            <svd:operationStatus>
                <svd:errorCode>0</svd:errorCode>
                <svd:errorMessage>errorMessage0</svd:errorMessage>
            </svd:operationStatus>
            <svd:dealID>Deal-ID-XX</svd:dealID>
        </ns1:saveDealRs>
    </xsl:template>
</xsl:stylesheet>