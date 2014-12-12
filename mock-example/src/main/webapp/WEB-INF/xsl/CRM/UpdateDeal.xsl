<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/UpdateDealRq/"
                xmlns:rs="http://sbrf.ru/NCP/CRM/UpdateDealRs/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/UpdateDealRs/Data/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="soap-env:Envelope">
        <xsl:element name="soap-env:Envelope">
            <xsl:copy-of select="soap-env:Header"/>
            <soap-env:Body>
                <xsl:variable name="data" select="document('../../data/CRM/xml/UpdateDealData.xml')/rsd:data"/>
                <xsl:variable name="linkedTag" select="./soap-env:Body/crm:UpdateDealRq/rq:comment"/>
                <xsl:call-template name="UpdateDealRs">
                    <xsl:with-param name="data" select="$data"/>
                    <xsl:with-param name="response">
                        <xsl:choose>
                            <xsl:when test="count($data/rsd:response[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>
                            <xsl:otherwise>default</xsl:otherwise>
                        </xsl:choose>
                    </xsl:with-param>
                </xsl:call-template>
            </soap-env:Body>
        </xsl:element>
    </xsl:template>


    <!--Fill tags with data from data.xml (0..1)-->
    <xsl:template match="rsd:errorMessage">
        <rs:errorMessage>
            <xsl:value-of select="."/>
        </rs:errorMessage>
    </xsl:template>

    <!--Transform main XML-->
    <xsl:template name="UpdateDealRs">
        <!--Get params-->
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <!-- - - - - - - - -->
        <crm:UpdateDealRs>
            <rs:operationStatus>
                <rs:errorCode>
                    <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
                </rs:errorCode>
                <!--Optional:-->
                <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            </rs:operationStatus>
            <rs:dealID>
                <xsl:value-of select="./soap-env:Body/crm:updateDealRq/rq:deal/rq:dealID"/>
            </rs:dealID>
        </crm:UpdateDealRs>
    </xsl:template>

</xsl:stylesheet>