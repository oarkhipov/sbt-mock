<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:soap-env="http://schemas.xmlsoap.org/soap/envelope/"
                xmlns:rq="http://sbrf.ru/NCP/CRM/ForceSignalRq/"
                xmlns:rs="http://sbrf.ru/NCP/CRM/ForceSignalRs/"
                xmlns:rsd="http://sbrf.ru/NCP/CRM/ForceSignalRq/Data/"
                xmlns:crm="http://sbrf.ru/NCP/CRM/">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" version="1.0"/>

    <!-- OH MY GOD! -->
    <!-- IT'S A -->
    <!-- DRIVER!!! -->

    <!--Prepare data and section of data XML-->
    <xsl:template match="*">
        <xsl:element name="soap-env:Envelope">
            <xsl:choose>
                <xsl:when test="soap-env:Header"><xsl:copy-of select="soap-env:Header"/></xsl:when>
                <xsl:otherwise><soap-env:Header/></xsl:otherwise>
            </xsl:choose>
            <soap-env:Body>
                <!--xsl:variable name="data" select="document('../../data/CRM/xml/ForceSignalRequestData.xsd')/rsd:data"/-->
                <xsl:variable name="data" select="."/>
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
        <crm:forceSignalRq xmlns:crm="http://sbrf.ru/NCP/CRM/">
            <rq:contractID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:contractID"/></rq:contractID>
            <rq:contractBPMID><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:contractBPMID"/></rq:contractBPMID>
            <rq:status><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:status"/></rq:status>
            <rq:comment><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:comment"/></rq:comment>
            <rq:requestType><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:requestType"/></rq:requestType>
            <rq:fullNameOfResponsiblePerson><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:fullNameOfResponsiblePerson"/></rq:fullNameOfResponsiblePerson>
            <!--Zero or more repetitions:-->
            <rq:participantsGroup>
                <rq:id><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:participantsGroup/rsd:id"/></rq:id>
                <rq:label><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:participantsGroup/rsd:label"/></rq:label>
                <rq:status><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:participantsGroup/rsd:status"/></rq:status>
                <rq:updateDate><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:participantsGroup/rsd:updateDate"/></rq:updateDate>
                <rq:approvalDate><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:participantsGroup/rsd:approvalDate"/></rq:approvalDate>
                <rq:topLevelGroupName><xsl:value-of select="$data/rsd:request[@name=$response]/rsd:participantsGroup/rsd:topLevelGroupName"/></rq:topLevelGroupName>
            </rq:participantsGroup>
        </crm:forceSignalRq>
    </xsl:template>

</xsl:stylesheet>