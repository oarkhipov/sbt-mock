<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/ForceSignalRq/"
                xmlns:rs="http://sbrf.ru/NCP/CRM/ForceSignalRs/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRs/Data/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!-- OH MY GOD! -->
    <!-- IT'S A -->
    <!-- DRIVER!!! -->

    <!--Prepare data and section of data XML-->
    <xsl:template match="soap-env:Envelope">
        <xsl:element name="soap-env:Envelope">
            <xsl:copy-of select="soap-env:Header"/>
            <soap-env:Body>
                <xsl:variable name="data" select="document('../../data/CRM/xml/ForceSignalData.xml')/rsd:data"/>
                <xsl:variable name="linkedTag" select="./soap-env:Body/crm:ForceSignalRq/rq:comment"/>
                <xsl:call-template name="forceSignal">
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
    <xsl:template name="forceSignal">
        <!--Get params-->
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <!-- - - - - - - - -->
        <crm:forceSignalRs>
            <rs:contractID>
                <xsl:value-of select="./soap-env:Body/crm:forceSignalRq/rq:contractID"/>
            </rs:contractID>
            <rs:contractBPMID>
                <xsl:value-of select="./soap-env:Body/crm:forceSignalRq/rq:contractBPMID"/>
            </rs:contractBPMID>
            <rs:status>
                <xsl:value-of select="./soap-env:Body/crm:forceSignalRq/rq:status"/>
            </rs:status>
            <rs:comment>
                <xsl:value-of select="./soap-env:Body/crm:forceSignalRq/rq:comment"/>
            </rs:comment>
            <rs:requestType>
                <xsl:value-of select="./soap-env:Body/crm:forceSignalRq/rq:requestType"/>
            </rs:requestType>
            <rs:fullNameOfResponsiblePerson>
                <xsl:value-of select="./soap-env:Body/crm:forceSignalRq/rq:fullNameOfResponsiblePerson"/>
            </rs:fullNameOfResponsiblePerson>
            <!-- 1 -->
            <rs:errorCode>
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
            </rs:errorCode>
            <!-- 0..N -->
            <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
        </crm:forceSignalRs>
    </xsl:template>

</xsl:stylesheet>