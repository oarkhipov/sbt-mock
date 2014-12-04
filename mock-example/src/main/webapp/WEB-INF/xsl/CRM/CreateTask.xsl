<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/CreateTaskRq/" xmlns:ns2="http://sbrf.ru/NCP/CRM/ForceSignalRs/"
                xmlns:ns1="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <xsl:template match="soap-env:Envelope">
        <xsl:element name="soap-env:Envelope">
            <xsl:copy-of select="soap-env:Header"/>
            <soap-env:Body>
                <xsl:variable name="data" select="document('../../xml/CRM_data/CreateTask.xml')/data"/>
                <xsl:variable name="comment" select="./soap-env:Body/rq:createTaskRq/rq:comment"/>
                <xsl:call-template name="createTaskRs">
                    <xsl:with-param name="data" select="$data"/>
                    <xsl:with-param name="response">
                        <xsl:choose>
                            <xsl:when test="count($data/response[@name=$comment])=1"><xsl:value-of select="$comment"/></xsl:when>
                            <xsl:otherwise>default</xsl:otherwise>
                        </xsl:choose>
                    </xsl:with-param>
                </xsl:call-template>
            </soap-env:Body>
        </xsl:element>
    </xsl:template>

    <xsl:template match="response">
        <xsl:apply-templates select="errorMessage"/>
    </xsl:template>

    <xsl:template match="errorMessage">
        <ns2:errorMessage>
            <xsl:value-of select="."/>
        </ns2:errorMessage>
    </xsl:template>

    <xsl:template name="createTaskRs">
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <ns2:createTaskRs>
            <xsl:apply-templates select="$data/response[@name=$response]"/>
            <ns2:errorCode>
                <xsl:value-of select="$data/response[@name=$response]/errorCode"/>
            </ns2:errorCode>
            <ns2:contractID>
                <xsl:value-of select="./soap-env:Body/rq:createTaskRq/rq:contractID"/>
            </ns2:contractID>
            <ns2:fullNameOfResponsiblePerson>
                <xsl:value-of select="./soap-env:Body/rq:createTaskRq/rq:fullNameOfResponsiblePerson"/>
            </ns2:fullNameOfResponsiblePerson>
            <ns2:comment>
                <xsl:value-of select="./soap-env:Body/rq:createTaskRq/rq:comment"/>
            </ns2:comment>
            <ns2:contractBPMID>
                <xsl:value-of select="./soap-env:Body/rq:createTaskRq/rq:contractBPMID"/>
            </ns2:contractBPMID>
            <ns2:requestType>
                <xsl:value-of select="./soap-env:Body/rq:createTaskRq/rq:requestType"/>
            </ns2:requestType>
        </ns2:createTaskRs>
    </xsl:template>

</xsl:stylesheet>