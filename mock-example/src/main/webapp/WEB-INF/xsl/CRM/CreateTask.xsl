<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/CreateTaskRq/"
                xmlns:ns2="http://sbrf.ru/NCP/CRM/CreateTaskRs/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/CreateTaskRs/Data/"
                xmlns:ns1="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!--Prepare data and section of data XML-->
    <xsl:template match="soap-env:Envelope">
        <xsl:element name="soap-env:Envelope">
            <xsl:copy-of select="soap-env:Header"/>
            <soap-env:Body>
                <xsl:variable name="data" select="document('../../data/CRM/xml/CreateTask.xml')/rsd:data"/>
                <xsl:variable name="linkedTag" select="./soap-env:Body/rq:createTaskRq/rq:comment"/>
                <xsl:call-template name="createTaskRs">
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

    <!--Fill blocks with data from data.xml (0..N)-->
    <!--<xsl:template match="response">-->
        <!--<xsl:apply-templates select="errorMessage"/>-->
    <!--</xsl:template>-->

    <!--Fill tags with data from data.xml (0..1)-->
    <xsl:template match="rsd:errorMessage">
        <ns2:errorMessage>
            <xsl:value-of select="."/>
        </ns2:errorMessage>
    </xsl:template>

    <!--Transform main XML-->
    <xsl:template name="createTaskRs">
        <!--Get params-->
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <!-- - - - - - - - -->
        <ns2:createTaskRs>
            <!-- 0..N -->
            <xsl:apply-templates select="$data/rsd:response[@name=$response]/rsd:errorMessage"/>
            <!-- 1 -->
            <ns2:errorCode>
                <xsl:value-of select="$data/rsd:response[@name=$response]/rsd:errorCode"/>
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