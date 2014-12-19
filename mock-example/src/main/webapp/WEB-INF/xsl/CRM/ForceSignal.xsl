<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://sbrf.ru/NCP/esb/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/ForceSignalRq/"
                xmlns:rs="http://sbrf.ru/NCP/CRM/ForceSignalRs/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <xsl:param name="name" select="all"/>
    <xsl:param name="timestamp" select="string('2014-12-16T17:55:06.410+04:00')"/>
    <xsl:param name="id" select="null"/>
    <xsl:param name="defaultId" select="string('5f4e83ab38514920b55f3eaa1dc378ad')"/>
    <!-- OH MY GOD! -->
    <!-- IT'S A -->
    <!-- DRIVER!!! -->

    <!--Prepare data and section of data XML-->
    <xsl:template match="*">
        <xsl:element name="soap-env:Envelope">
            <xsl:call-template name="soap-env:Header"/>
            <soap-env:Body>
                <!--xsl:variable name="data" select="document('../../data/CRM/xml/ForceSignalRequestData.xsd')/rsd:data"/-->
                <xsl:variable name="data" select="."/>
                <xsl:variable name="linkedTag" select="$name"/>
                <xsl:call-template name="forceSignal">
                    <xsl:with-param name="data" select="$data"/>
                    <xsl:with-param name="response">
                        <xsl:choose>
                            <xsl:when test="count($data/rsd:request[@name=$linkedTag])=1"><xsl:value-of select="$linkedTag"/></xsl:when>
                            <xsl:otherwise>default</xsl:otherwise>
                        </xsl:choose>
                    </xsl:with-param>
                </xsl:call-template>
            </soap-env:Body>
        </xsl:element>
    </xsl:template>

    <xsl:template name="soap-env:Header">
        <soap-env:Header>
            <!--<soap-env:message-id><xsl:value-of select="concat(./soap-env:message-id,substring('5f4e83ab38514920b55f3eaa1dc378ad', 1 div mot(./soap-env:message-id)))"/></soap-env:message-id>-->
            <soap-env:message-id>
                <xsl:choose>
                    <xsl:when test="$id!='null'"><xsl:value-of select="$id"/></xsl:when>
                    <xsl:when test="soap-env:message-id"><xsl:value-of select="soap-env:message-id"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$defaultId"/></xsl:otherwise>
                </xsl:choose>
            </soap-env:message-id>
            <soap-env:request-time><xsl:value-of select="$timestamp"/></soap-env:request-time>
            <soap-env:operation-name>forceSignal</soap-env:operation-name>
        </soap-env:Header>
    </xsl:template>

    <!--Fill tags with data from data.xml (0..1)-->
    <xsl:template match="rsd:errorMessage">
        <rs:errorMessage>
            <xsl:value-of select="."/>
        </rs:errorMessage>
    </xsl:template>

    <xsl:template match="rsd:participantsGroup">
        <rq:participantsGroup>
            <rq:id><xsl:value-of select="./rsd:id"/></rq:id>
            <rq:label><xsl:value-of select="./rsd:label"/></rq:label>
            <rq:status><xsl:value-of select="./rsd:status"/></rq:status>
            <rq:updateDate><xsl:value-of select="./rsd:updateDate"/></rq:updateDate>
            <rq:approvalDate><xsl:value-of select="./rsd:approvalDate"/></rq:approvalDate>
            <rq:topLevelGroupName><xsl:value-of select="./rsd:topLevelGroupName"/></rq:topLevelGroupName>
        </rq:participantsGroup>
    </xsl:template>

    <!--Transform main XML-->
    <xsl:template name="forceSignal">
        <!--Get params-->
        <xsl:param name="response"/>
        <xsl:param name="data"/>
        <!-- - - - - - - - -->
        <crm:forceSignalRq xmlns:crm="http://sbrf.ru/NCP/CRM/">
            <rq:contractID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:contractID"/></rq:contractID>
            <rq:contractBPMID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:contractBPMID"/></rq:contractBPMID>
            <rq:status><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:status"/></rq:status>
            <rq:comment><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:comment"/></rq:comment>
            <rq:requestType><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:requestType"/></rq:requestType>
            <rq:fullNameOfResponsiblePerson><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:fullNameOfResponsiblePerson"/></rq:fullNameOfResponsiblePerson>
            <xsl:apply-templates select="$data/rsd:request[@name=$response]/rsd:participantsGroup"/>
        </crm:forceSignalRq>
    </xsl:template>

</xsl:stylesheet>